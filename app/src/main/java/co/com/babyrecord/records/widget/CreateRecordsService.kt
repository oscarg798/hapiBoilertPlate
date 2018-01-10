package co.com.babyrecord.records.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import co.com.babyrecord.TYPE
import co.com.babyrecord.Utils
import co.com.core.models.Record
import co.com.core.use_cases.record.CreateRecordUseCase
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by oscarg798 on 1/6/18.
 */
class CreateRecordsService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onHandleIntent(intent)
        return Service.START_NOT_STICKY
    }

    private fun onHandleIntent(intent: Intent?) {
        intent?.hasExtra(TYPE)?.let {
            CreateRecordUseCase(Schedulers.io(),
                    Schedulers.io())
                    .execute(Record(UUID.randomUUID().toString(), Calendar.getInstance().timeInMillis,
                            null, intent.getStringExtra(TYPE)),
                            object : DisposableSingleObserver<Record>() {
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

    }
}