package co.com.babyrecord.create_or_edit_baby

import co.com.babyrecord.IBaseView
import co.com.core.models.Baby

/**
 * Created by oscarg798 on 12/21/17.
 */
interface ICreateOrEditBabyActivityView : IBaseView {

    fun showName(name: String)

    fun showBirthday(birthDay: String)

    fun onBabySaved(baby: Baby)

    fun showBirthdayError()


}