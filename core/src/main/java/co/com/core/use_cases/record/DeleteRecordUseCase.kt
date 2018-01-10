package co.com.core.use_cases.record

import co.com.core.models.Record
import co.com.core.use_cases.SingleUseCase
import co.com.data.persistence.PersistenceFactory
import co.com.data.persistence.entities.DBRecord
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by oscarg798 on 12/21/17.
 */
class DeleteRecordUseCase(mSubcribeOnScheduler: Scheduler,
                          mObseverOnScheduler: Scheduler) :
        SingleUseCase<String, Record>(mSubcribeOnScheduler, mObseverOnScheduler) {
    override fun buildUseCase(params: Record): Single<String> =
            Single.create { emitter ->
                PersistenceFactory.instance.getDatabase().recordDAO()
                        .delete(DBRecord(params.uuid,
                                params.startTime,
                                params.endTime,
                                params.type))

                emitter.onSuccess(params.uuid)
            }
}