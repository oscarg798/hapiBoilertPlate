package co.com.core.use_cases

import io.reactivex.observers.DisposableSingleObserver

/**
 * Created by oscarg798 on 12/20/17.
 */
interface ISingleUseCase<Response, Params>{

    fun execute(params: Params, observer: DisposableSingleObserver<Response>)

    fun dispose()
}