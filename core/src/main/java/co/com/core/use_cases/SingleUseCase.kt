package co.com.core.use_cases

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver

/**
 * Created by oscarg798 on 12/20/17.
 */
abstract class SingleUseCase<Response, Params>(private val mSubscribeOnScheduler: Scheduler,
                                               private val mObserverOnScheduler: Scheduler) :
        ISingleUseCase<Response, Params> {

    private val mCompositeDisposables = CompositeDisposable()

    abstract fun buildUseCase(params: Params): Single<Response>

    override fun execute(params: Params, observer: DisposableSingleObserver<Response>) {
        val observable = buildUseCase(params)
                .subscribeOn(mSubscribeOnScheduler)
                .observeOn(mObserverOnScheduler)

        mCompositeDisposables.add(observable.subscribeWith(observer))
    }

    override fun dispose() {
        mCompositeDisposables.dispose()
        mCompositeDisposables.clear()
    }


}