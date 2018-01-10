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
class SaveSizeRecordUseCase(mSubscribeOnScheduler: Scheduler,
                            mObserverOnScheduler: Scheduler) :
        SingleUseCase<SizeRecord, SizeRecord>(mSubscribeOnScheduler, mObserverOnScheduler) {

    override fun buildUseCase(params: SizeRecord): Single<SizeRecord> {
        return Single.create { emitter ->

            PersistenceFactory.instance.getDatabase().sizeRecordDAO()
                    .insert(DBSizeRecord(height = params.height,
                            weight = params.weight,
                            date = params.date))

            emitter.onSuccess(params)

        }
    }
}