package co.com.core.use_cases.record

import co.com.core.models.Record
import co.com.core.use_cases.SingleUseCase
import co.com.data.persistence.PersistenceFactory
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

/**
 * Created by oscarg798 on 1/8/18.
 */
class GetRecordsByTypeUseCase(mSubcribeOnScheduler: Scheduler,
                              mObseverOnScheduler: Scheduler) :
        SingleUseCase<List<Record>, Pair<Long, String>>(mSubcribeOnScheduler, mObseverOnScheduler) {


    override fun buildUseCase(params: Pair<Long, String>): Single<List<Record>> {
        return Single.create { emitter ->

            val calendar = Calendar.getInstance()
            val paramsCalendar = Calendar.getInstance()
            paramsCalendar.timeInMillis = params.first

            val records = PersistenceFactory.instance.getDatabase().recordDAO().getRecords()
                    .mapTo(ArrayList(), {
                        Record(it.uuid, it.startTime, it.endTime, it.type)
                    })

            val recordsToReturn = ArrayList<Record>()

            records.filter {
                calendar.timeInMillis = it.startTime
                calendar.get(Calendar.YEAR) == paramsCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == paramsCalendar.get(Calendar.DAY_OF_YEAR)
                        && it.type == params.second
            }.forEach {
                recordsToReturn.add(it)
            }


            emitter.onSuccess(recordsToReturn)
        }
    }
}