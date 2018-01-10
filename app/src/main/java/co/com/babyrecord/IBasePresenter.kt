package co.com.babyrecord

import android.content.Intent

/**
 * Created by oscarg798 on 12/20/17.
 */
interface IBasePresenter<in T : IBaseView> {

    fun bind(view: T)

    fun unBind()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}