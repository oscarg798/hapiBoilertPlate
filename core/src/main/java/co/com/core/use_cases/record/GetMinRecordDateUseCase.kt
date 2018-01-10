package co.com.core.use_cases.record

import co.com.core.use_cases.SingleUseCase
import co.com.data.persistence.PersistenceFactory
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

/**
 * Created by oscarg798 on 1/5/18.
 */
class GetMinRecordDateUseCase(mSubscribeOnScheduler: Scheduler,
                              mObserverOnScheduler: Scheduler) :
        SingleUseCase<Long, Any?>(mSubscribeOnScheduler, mObserverOnScheduler) {

    override fun buildUseCase(params: Any?): Single<Long> {
        return Single.create { emitter ->

            var minDate = PersistenceFactory.instance
                    .getDatabase().recordDAO().getRecordsMinDate()

            if (minDate == 0.toLong()) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, -1)
                minDate = calendar.timeInMillis
            }

            emitter.onSuccess(minDate)


        }
    }
}