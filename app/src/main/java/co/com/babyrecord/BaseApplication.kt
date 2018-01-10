package co.com.babyrecord

import android.app.Application
import android.content.Context
import co.com.core.use_cases.app.InitDataUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by oscarg798 on 12/20/17.
 */
class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        val initDataUseCase = InitDataUseCase(Schedulers.io(),
                AndroidSchedulers.mainThread())

        initDataUseCase.execute(this, object : DisposableCompletableObserver() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }


    companion object {
        lateinit var instance: BaseApplication
    }
}