package co.com.babyrecord.dashboard

import android.app.Fragment
import co.com.babyrecord.IBaseView

/**
 * Created by oscarg798 on 12/22/17.
 */
interface IDashboardActivityView : IBaseView {

    fun changeFragment(fragment: Fragment, tag: String?)

    fun setTitle(title: String)
}