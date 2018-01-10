package co.com.babyrecord.size_record

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import co.com.babyrecord.BaseApplication
import co.com.babyrecord.CREAtE_SIZE_RECORD_REQUEST_CODE
import co.com.babyrecord.R
import co.com.babyrecord.SIZE_RECORD_KEY
import co.com.core.models.SizeRecord
import co.com.core.use_cases.size_record.DeleteSizeRecordUseCase
import co.com.core.use_cases.size_record.GetSizeRecordsUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by oscarg798 on 12/22/17.
 */
class SizeRecordFragmentPresenter : ISizeRecordFragmentPresenter {

    private var mView: ISizeRecordFragmentView? = null

    override fun bind(view: ISizeRecordFragmentView) {
        mView = view
        mView?.initComponents()
        getSizeRecords()
    }

    private fun getSizeRecords() {
        mView?.showProgressBar()
        GetSizeRecordsUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(null, object : DisposableSingleObserver<ArrayList<SizeRecord>>() {
                    override fun onSuccess(t: ArrayList<SizeRecord>) {
                        mView?.showSizeRecords(t)
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

    override fun deleteSizeRecord(sizeRecord: SizeRecord) {

        mView?.showConfirmationAlertDialog(BaseApplication.instance.
                getString(R.string.delete_size_record_confirmation_message),
                DialogInterface.OnClickListener { _, _ ->
                    mView?.showProgressBar()
                    DeleteSizeRecordUseCase(Schedulers.io(),
                            AndroidSchedulers.mainThread())
                            .execute(sizeRecord, object : DisposableSingleObserver<String>() {
                                override fun onSuccess(t: String) {
                                    mView?.removeSizeRecord(t)
                                    mView?.hideProgressBar()
                                    this.dispose()
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    mView?.hideProgressBar()
                                    this.dispose()
                                }
                            })

                })

    }

    override fun getSizeObservable(): DisposableObserver<Int> {
        return object : DisposableObserver<Int>() {
            override fun onError(e: Throwable) {
                e.printStackTrace()

            }

            override fun onComplete() {
            }

            override fun onNext(t: Int) {
                if (t > 0) {
                    mView?.hideSizeRecordMessage()
                } else {
                    mView?.showNoSizeRecordMessage()
                }
            }
        }
    }

    override fun unBind() {
        mView = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREAtE_SIZE_RECORD_REQUEST_CODE && resultCode == RESULT_OK &&
                data !== null && data.hasExtra(SIZE_RECORD_KEY)) {
            mView?.showSizeRecords(arrayListOf(data.getParcelableExtra(SIZE_RECORD_KEY)))
        }
    }
}