package co.com.babyrecord.profile

import co.com.babyrecord.IBasePresenter
import co.com.core.models.Baby

/**
 * Created by oscarg798 on 12/22/17.
 */
interface IBabyProfileFragmentPresenter : IBasePresenter<IBabyProfileFragmentView>{
    fun getBaby(): Baby?

}