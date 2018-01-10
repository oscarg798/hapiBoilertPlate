package co.com.core.use_cases.size_record

import co.com.core.models.SizeRecord
import co.com.core.use_cases.SingleUseCase
import co.com.data.persistence.PersistenceFactory
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by oscarg798 on 12/22/17.
 */
class GetSizeRecordsUseCase(mSubscribeOnScheduler: Scheduler,
                            mObserverOnScheduler: Scheduler) :
        SingleUseCase<ArrayList<SizeRecord>, Any?>(mSubscribeOnScheduler, mObserverOnScheduler) {

    override fun buildUseCase(params: Any?): Single<ArrayList<SizeRecord>> {
        return Single.create { emitter ->

            emitter.onSuccess(PersistenceFactory.instance.getDatabase().sizeRecordDAO().get()
                    .mapTo(ArrayList(), {
                        SizeRecord(it.uuid, it.height, it.weight, it.date)
                    }))
        }
    }
}