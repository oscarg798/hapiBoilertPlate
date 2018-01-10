package co.com.babyrecord.records.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import co.com.babyrecord.R
import co.com.babyrecord.RECORD
import co.com.babyrecord.Utils
import co.com.babyrecord.VIEW_ID
import co.com.core.models.Record
import co.com.core.use_cases.record.DeleteRecordUseCase
import co.com.core.use_cases.record.UpdateRecordUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by oscarg798 on 1/6/18.
 */
class RunAnActionOnRecordFromWidgetIntentService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        onHandleIntent(intent)
        return Service.START_NOT_STICKY
    }

    private fun onHandleIntent(intent: Intent?) {
        intent?.extras?.let {
            if (intent.hasExtra(RECORD) && intent.hasExtra(VIEW_ID)) {
                when (intent.getIntExtra(VIEW_ID, 0)) {
                    R.id.mTVDeleteRecord -> deletRecord(intent.getParcelableExtra(RECORD))
                    R.id.mTVSetEndTimeToRecord -> setEndTime(intent.getParcelableExtra(RECORD))
                }

            }
        }
    }

    private fun setEndTime(record: Record) {
        record.endTime = Calendar.getInstance().timeInMillis
        UpdateRecordUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())
                .execute(record, object : DisposableSingleObserver<Record>() {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        this.dispose()
                    }

                    override fun onSuccess(t: Record) {
                        Utils.Companion.instance.requestWidgetUpdate(applicationContext)
                        this.dispose()
                    }
                })
    }

    private fun deletRecord(record: Record) {
        DeleteRecordUseCase(Schedulers.io(),
                Schedulers.io())
                .execute(record,
                        object : DisposableSingleObserver<String>() {
                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                this.dispose()
                            }

                            override fun onSuccess(t: String) {
                                Utils.Companion.instance.requestWidgetUpdate(applicationContext)
                                this.dispose()
                            }
                        })
    }


}