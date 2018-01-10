package co.com.core.use_cases.baby

import co.com.core.models.Baby
import co.com.core.use_cases.SingleUseCase
import co.com.data.persistence.PersistenceFactory
import co.com.data.persistence.entities.DBBaby
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by oscarg798 on 12/21/17.
 */
class CreateBabyUseCase(mSubscribeOnScheduler: Scheduler,
                        mObserverOnScheduler: Scheduler) :
        SingleUseCase<Baby, Baby>(mSubscribeOnScheduler, mObserverOnScheduler) {

    override fun buildUseCase(params: Baby): Single<Baby> {
        return Single.create { emitter ->

            PersistenceFactory.instance.getDatabase().babyDAO().
                    insert(DBBaby(name = params.name,
                    birthDate = params.birthDate))

            emitter.onSuccess(params)


        }
    }
}
