package co.com.babyrecord.records

import android.content.DialogInterface
import android.content.Intent
import co.com.babyrecord.*
import co.com.core.models.Record
import co.com.core.use_cases.record.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by oscarg798 on 12/20/17.
 */
class RecordsFragmentPresenter : IRecordsFragmentPresenter {

    private var mView: IRecordsFragmentView? = null

    private var mRecords: List<Record>? = null

    private val mCalendar = Calendar.getInstance()

    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    private var mCurrentRecordsCalendar: Calendar = Calendar.getInstance()

    private var mSelectedTypeFromTabSelection = SLEEP_TYPE

    override fun bind(view: IRecordsFragmentView) {
        mView = view
        mView?.initComponents()
        if (mRecords === null) {
            getRecords(Calendar.getInstance().timeInMillis)
        } else {
            deliverRecords()
        }
        mView?.setRecordsDate(mSimpleDateFormat.format(mCurrentRecordsCalendar.time))
        mView?.hideIVNext()

    }

    override fun unBind() {
        mView = null
    }

    private fun getRecords(date: Long) {
        mView?.showProgressBar()
        GetRecordsByTypeUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(Pair(date, mSelectedTypeFromTabSelection),
                        object : DisposableSingleObserver<List<Record>>() {
                            override fun onSuccess(t: List<Record>) {
                                mRecords = t
                                deliverRecords()
                                mView?.hideProgressBar()
                                this.dispose()
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                mView?.hideProgressBar()
                                this.dispose()
                            }
                        })

    }

    private fun deliverRecords() {
        mRecords?.let {
            mView?.showRecords(ArrayList(mRecords))
        }


    }

    override fun creteRecord(viewID: Int) {
        mView?.showProgressBar()

        CreateRecordUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread()).
                execute(Record(UUID.randomUUID().toString(),
                        Calendar.getInstance().timeInMillis,
                        when (viewID) {
                            R.id.mFABCreateMedicineRecord -> Calendar.getInstance().timeInMillis
                            else -> null
                        }, when (viewID) {
                    R.id.mFABCreateSleepRecord -> SLEEP_TYPE
                    R.id.mFABCreateFeedRecord -> FEED_TYPE
                    else -> MEDICINE_TYPE
                }),
                        object : DisposableSingleObserver<Record>() {
                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                mView?.hideProgressBar()
                                this.dispose()
                            }

                            override fun onSuccess(t: Record) {
                                if(t.type == mSelectedTypeFromTabSelection){
                                    mView?.addRecord(t)
                                }
                                mView?.hideProgressBar()
                                this.dispose()

                            }
                        })
    }

    override fun updateRecord(record: Record) {
        UpdateRecordUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(record, object : DisposableSingleObserver<Record>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView?.hideProgressBar()
                        this.dispose()
                    }

                    override fun onSuccess(t: Record) {
                        if(t.type == mSelectedTypeFromTabSelection){
                            mView?.updateRecord(record)
                        }
                        mView?.hideProgressBar()
                        this.dispose()
                    }
                })


    }

    override fun finishRecord(record: Record) {
        record.endTime = Calendar.getInstance().timeInMillis
        updateRecord(record)
    }

    override fun onRefresh() {
        mView?.clearRecords()
        getRecords(mCurrentRecordsCalendar.timeInMillis)
        val currentCalendar = Calendar.getInstance()
        mView?.showHideFabMenuCreateRecords(currentCalendar.get(Calendar.YEAR) == mCurrentRecordsCalendar.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.DAY_OF_YEAR) == mCurrentRecordsCalendar.get(Calendar.DAY_OF_YEAR))
        val currentDateCalendar = Calendar.getInstance()
        if (currentDateCalendar.get(Calendar.YEAR) ==
                mCurrentRecordsCalendar.get(Calendar.YEAR)
                && currentDateCalendar.get(Calendar.DAY_OF_YEAR) ==
                mCurrentRecordsCalendar.get(Calendar.DAY_OF_YEAR)) {
            mView?.hideIVNext()
        } else {
            mView?.showIVNext()
        }
    }

    override fun deleteRecord(record: Record) {

        mView?.showConfirmationAlertDialog(BaseApplication.instance.getString(R.string.delete_record_confirmation_message),
                DialogInterface.OnClickListener { _, _ ->
                    mView?.showProgressBar()
                    DeleteRecordUseCase(Schedulers.io(),
                            AndroidSchedulers.mainThread())
                            .execute(record, object : DisposableSingleObserver<String>() {
                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    mView?.hideProgressBar()
                                    this.dispose()
                                }

                                override fun onSuccess(t: String) {
                                    mView?.removeRecord(t)
                                    mView?.hideProgressBar()
                                    this.dispose()
                                }

                            })


                })

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCurrentRecordsCalendar.set(year, monthOfYear, dayOfMonth)
        mView?.setRecordsDate(mSimpleDateFormat.format(mCurrentRecordsCalendar.time))
        onRefresh()
    }

    override fun editRecordEndTime(record: Record) {
        val minDateCalendar = Calendar.getInstance()
        minDateCalendar.timeInMillis = record.startTime
        val maxDateCalendar = Calendar.getInstance()
        maxDateCalendar.timeInMillis = minDateCalendar.timeInMillis
        maxDateCalendar.add(Calendar.WEEK_OF_YEAR, 1)
        mView?.showDateDialog(minDateCalendar, maxDateCalendar, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            maxDateCalendar.set(year, monthOfYear, dayOfMonth)
            record.endTime = maxDateCalendar.timeInMillis
            continueWithEditRecordEndTime(record)
        })

    }

    private fun continueWithEditRecordEndTime(record: Record) {
        mView?.showTimeDialog(TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->
            val endTimeCalendar = Calendar.getInstance()
            endTimeCalendar.timeInMillis = record.endTime!!
            endTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endTimeCalendar.set(Calendar.MINUTE, minute)
            endTimeCalendar.set(Calendar.SECOND, second)
            record.endTime = endTimeCalendar.timeInMillis
            if (record.startTime <= record.endTime!!) {
                updateRecord(record)
            } else {
                view.activity?.let {
                    mView?.showMessage(view.activity.resources.getString(R.string.end_time_lower_than_start_time_message))
                }
            }

        })
    }

    override fun changeDate() {
        val maxDate = Calendar.getInstance()
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -1)
        mView?.showDateDialog(minDate, maxDate, this)
    }

    override fun onTabClickListener(viewID: Int) {
        mSelectedTypeFromTabSelection = when (viewID) {
            R.id.mIVSleepTab -> SLEEP_TYPE
            R.id.mIVFeedTab -> FEED_TYPE
            else -> MEDICINE_TYPE
        }

        onRefresh()

    }

    override fun changeDateIVPresed(id: Int) {
        when (id) {
            R.id.mIVNext -> mCurrentRecordsCalendar.add(Calendar.DAY_OF_YEAR, 1)
            else -> mCurrentRecordsCalendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        mView?.setRecordsDate(mSimpleDateFormat.format(mCurrentRecordsCalendar.time))
        onRefresh()
    }

    override fun getSleepRecordByDateFromRecord(record: Record): SleepRecordsByDate {
        mCalendar.timeInMillis = record.startTime
        return SleepRecordsByDate(mSimpleDateFormat.format(Date(record.startTime)),
                arrayListOf(record), mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.DAY_OF_YEAR))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}