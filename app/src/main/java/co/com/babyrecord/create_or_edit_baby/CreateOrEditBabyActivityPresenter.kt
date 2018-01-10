package co.com.babyrecord.create_or_edit_baby

import android.content.Intent
import android.os.Bundle
import co.com.babyrecord.BABY_KEY
import co.com.core.models.Baby
import co.com.core.use_cases.baby.CreateBabyUseCase
import co.com.core.use_cases.baby.UpdateBabyUseCase
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oscarg798 on 12/21/17.
 */
class CreateOrEditBabyActivityPresenter : ICreateOrEditBabyActivityPresenter {


    private var mView: ICreateOrEditBabyActivityView? = null

    private var mBirthdaySelected: Long? = null

    private val mCalendar = Calendar.getInstance()

    private var mBaby: Baby? = null

    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy",
            Locale.getDefault())


    override fun bind(view: ICreateOrEditBabyActivityView) {
        mView = view
        mView?.initComponents()
    }


    override fun bindArguments(arguments: Bundle?) {
        if (arguments !== null && arguments.containsKey(BABY_KEY)) {
            mBaby = arguments.getParcelable(BABY_KEY)
            mBaby?.let {
                mView?.showName(mBaby!!.name)
                mView?.showBirthday(mSimpleDateFormat.format(Date(mBaby!!.birthDate)))
                mBirthdaySelected = mBaby!!.birthDate
            }
        }
    }


    override fun unBind() {
        mView = null
    }

    override fun saveBaby(name: String) {
       if(mBirthdaySelected!==null) {
            val baby = Baby(name, mBirthdaySelected!!,
                    if (mBaby !== null) mBaby!!.weight else null,
                    if (mBaby !== null) mBaby!!.height else null)

            if (mBaby!==null) {
                updateBaby(baby)
            } else {
                createBaby(baby)
            }
        }else{
           mView?.showBirthdayError()
       }

    }

    private fun createBaby(baby: Baby) {
        mView?.showProgressBar()
        CreateBabyUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(baby, object : DisposableSingleObserver<Baby>() {
                    override fun onSuccess(t: Baby) {
                        mView?.onBabySaved(t)
                        mView?.hideProgressBar()
                        this.dispose()
                    }

                    override fun onError(e: Throwable) {
                        mView?.hideProgressBar()
                        this.dispose()
                    }

                })

    }

    private fun updateBaby(baby: Baby) {
        mView?.showProgressBar()
        UpdateBabyUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(baby, object : DisposableSingleObserver<Baby>() {
                    override fun onSuccess(t: Baby) {
                        mView?.onBabySaved(t)
                        mView?.hideProgressBar()
                        this.dispose()
                    }

                    override fun onError(e: Throwable) {
                        mView?.hideProgressBar()
                        this.dispose()
                    }

                })
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mCalendar.set(year, monthOfYear, dayOfMonth)
        mBirthdaySelected = mCalendar.timeInMillis
        mBirthdaySelected?.let {
            mView?.showBirthday(mSimpleDateFormat.format(Date(mBirthdaySelected!!)))
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}