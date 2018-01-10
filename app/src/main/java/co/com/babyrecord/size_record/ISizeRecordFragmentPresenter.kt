package co.com.babyrecord.size_record

import co.com.babyrecord.IBasePresenter
import io.reactivex.observers.DisposableObserver

/**
 * Created by oscarg798 on 12/22/17.
 */
interface ISizeRecordFragmentPresenter : IBasePresenter<ISizeRecordFragmentView>,
        ISizeRecordCallbacks {

    fun getSizeObservable():DisposableObserver<Int>

}