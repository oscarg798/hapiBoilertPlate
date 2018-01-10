package co.com.babyrecord.add_size_record

import android.content.Intent
import co.com.core.models.SizeRecord
import co.com.core.use_cases.size_record.SaveSizeRecordUseCase
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oscarg798 on 12/22/17.
 */
class AddSizeRecordActivityPresenter : IAddSizeRecordActivityPresenter {

    private var mView: IAddSizeRecordActivityView? = null

    private val mCalendar = Calendar.getInstance()

    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())


    private var mDateSelected: Long? = null

    override fun bind(view: IAddSizeRecordActivityView) {
        mView = view
        mView?.initComponents()
    }

    override fun saveSizeRecord(height: Float, weight: Int) {
        if (mDateSelected !== null) {
            mView?.showProgressBar()
            SaveSizeRecordUseCase(Schedulers.io(),
                    AndroidSchedulers.mainThread())
                    .execute(SizeRecord(height = height, weight = weight, date = mDateSelected!!),
                            object : DisposableSingleObserver<SizeRecord>() {
                                override fun onSuccess(t: SizeRecord) {
                                    mView?.onSizeRecordSave(t)
                                    mView?.hideProgressBar()
                                    this.dispose()

                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    mView?.hideProgressBar()
                                    this.dispose()
                                }
                            })

        } else {
            mView?.showDateError()
        }
    }

    override fun unBind() {
        mView = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCalendar.set(year, monthOfYear, dayOfMonth)
        mDateSelected = mCalendar.timeInMillis
        mView?.showDate(mSimpleDateFormat.format(Date(mCalendar.timeInMillis)))

    }
}