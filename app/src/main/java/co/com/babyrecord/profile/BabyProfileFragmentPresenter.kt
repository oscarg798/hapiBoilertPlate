package co.com.babyrecord.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import co.com.babyrecord.BABY_KEY
import co.com.babyrecord.CREATE_OR_EDIT_BABY_REQUEST_CODE
import co.com.babyrecord.R
import co.com.babyrecord.Utils
import co.com.core.models.Baby
import co.com.core.use_cases.baby.GetBabyUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oscarg798 on 12/22/17.
 */
class BabyProfileFragmentPresenter : IBabyProfileFragmentPresenter {

    private var mView: IBabyProfileFragmentView? = null

    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy",
            Locale.getDefault())

    private var mBaby: Baby? = null

    override fun bind(view: IBabyProfileFragmentView) {
        mView = view
        mView?.initComponents()
        getBabyFromData()
    }

    private fun getBabyFromData() {
        mView?.showProgressBar()
        GetBabyUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(null, object : DisposableSingleObserver<Baby>() {
                    override fun onSuccess(t: Baby) {
                        mBaby = t
                        deliverBaby(t)
                        mView?.hideProgressBar()
                        this.dispose()

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView?.showNoBabyMessage()
                        mView?.hideProgressBar()
                        this.dispose()
                    }
                })
    }

    private fun deliverBaby(baby: Baby) {
        mView?.hideNoBabyMessage()
        mView?.showName(Utils.Companion.instance.boldTextPrefix("Name: ${baby.name}"))
        mView?.showBirthday(Utils.Companion.instance.
                boldTextPrefix("Birthday: ${mSimpleDateFormat.format(Date(baby.birthDate))}"))
        mView?.showBabyAge(baby.birthDate)

        if (baby.weight === null) {
            mView?.hideWeight()
        } else {
            mView?.showWeight(Utils.Companion.instance.boldTextPrefix("Weight:\n${baby.weight} pounds",
                    mView?.getViewContext()?.resources?.getColor(R.color.colorPrimary) ?: Color.BLACK))
        }

        if (baby.height === null) {
            mView?.hideHeight()
        } else {
            mView?.showHeight(Utils.Companion.instance.boldTextPrefix("Height:\n${baby.height} cm",
                    mView?.getViewContext()?.resources?.getColor(R.color.colorPrimary) ?: Color.BLACK))
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_OR_EDIT_BABY_REQUEST_CODE && resultCode == RESULT_OK &&
                data !== null && data.hasExtra(BABY_KEY)) {
            deliverBaby(data.getParcelableExtra(BABY_KEY))
        }
    }

    override fun unBind() {
        mView = null
    }

    override fun getBaby(): Baby? {
        return mBaby
    }
}