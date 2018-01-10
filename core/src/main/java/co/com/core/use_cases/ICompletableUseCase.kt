package co.com.core.use_cases

import io.reactivex.observers.DisposableCompletableObserver

/**
 * Created by oscarg798 on 12/20/17.
 */
interface ICompletableUseCase< Params> {

    fun execute(params: Params, observer: DisposableCompletableObserver)

    fun dispose()
}