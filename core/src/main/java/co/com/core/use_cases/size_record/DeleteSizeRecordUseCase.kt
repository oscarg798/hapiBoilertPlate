package co.com.core.use_cases.size_record

import co.com.core.models.SizeRecord
import co.com.core.use_cases.SingleUseCase
import co.com.data.persistence.PersistenceFactory
import co.com.data.persistence.entities.DBSizeRecord
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by oscarg798 on 12/22/17.
 */
class DeleteSizeRecordUseCase(mSubscribeOnScheduler: Scheduler,
                              mObserverOnScheduler: Scheduler) :
        SingleUseCase<String, SizeRecord>(mSubscribeOnScheduler, mObserverOnScheduler) {

    override fun buildUseCase(params: SizeRecord): Single<String> {
        return Single.create { emitter ->

            PersistenceFactory.instance.getDatabase().sizeRecordDAO()
                    .delete(DBSizeRecord(uuid = params.uuid,
                            height = params.height,
                            weight = params.weight,
                            date = params.date))

            emitter.onSuccess(params.uuid)

        }
    }
}