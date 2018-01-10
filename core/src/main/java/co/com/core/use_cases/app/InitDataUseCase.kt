package co.com.core.use_cases.app

import android.content.Context
import co.com.core.use_cases.CompletableUseCase
import co.com.data.persistence.PersistenceFactory
import io.reactivex.Completable
import io.reactivex.Scheduler

/**
 * Created by oscarg798 on 12/20/17.
 */
class InitDataUseCase(mSubscribeOnScheduler: Scheduler,
                      mObserverOnScheduler: Scheduler) : CompletableUseCase<Context>(mSubscribeOnScheduler, mObserverOnScheduler) {

    override fun buildUseCase(params: Context): Completable {
        return Completable.create { emiter ->
            PersistenceFactory.instance.initDatabase(params)
            emiter.onComplete()
        }
    }
}