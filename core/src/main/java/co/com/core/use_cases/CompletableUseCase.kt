package co.com.core.use_cases

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver

/**
 * Created by oscarg798 on 12/20/17.
 */
abstract class CompletableUseCase<Params>(private val mSubscribeOnScheduler: Scheduler,
                                          private val mObserverOnScheduler: Scheduler) :
        ICompletableUseCase<Params> {

    private val mCompositeDisposables = CompositeDisposable()

    abstract fun buildUseCase(params: Params): Completable

    override fun execute(params: Params, observer: DisposableCompletableObserver) {
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