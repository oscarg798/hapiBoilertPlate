package co.com.core.use_cases.record

import co.com.core.use_cases.SingleUseCase
import co.com.core.models.Record
import co.com.data.persistence.PersistenceFactory
import co.com.data.persistence.entities.DBRecord
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by oscarg798 on 12/20/17.
 */
class CreateRecordUseCase(mSubcribeOnScheduler: Scheduler,
                          mObseverOnScheduler: Scheduler) :
        SingleUseCase<Record, Record>(mSubcribeOnScheduler, mObseverOnScheduler) {

    override fun buildUseCase(params: Record): Single<Record> {
        return Single.create { emitter ->

            val database = PersistenceFactory.instance.getDatabase()
            database.recordDAO().
                    insert(DBRecord(params.uuid,
                            params.startTime,
                            params.endTime,
                            params.type))


            emitter.onSuccess(params)
        }
    }
}