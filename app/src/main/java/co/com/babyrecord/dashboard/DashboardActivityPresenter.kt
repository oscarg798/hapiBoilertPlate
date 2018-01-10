package co.com.babyrecord.dashboard

import android.app.Fragment
import android.content.Intent
import android.view.MenuItem
import co.com.babyrecord.BaseApplication
import co.com.babyrecord.R
import co.com.babyrecord.profile.BabyProfileFragment
import co.com.babyrecord.records.RecordsFragment
import co.com.babyrecord.size_record.SizeRecordFragment
import java.lang.ref.WeakReference

/**
 * Created by oscarg798 on 12/22/17.
 */
class DashboardActivityPresenter : IDashboardActivityPresenter {

    private var mView: IDashboardActivityView? = null

    private var mCurrentFragment: WeakReference<Fragment>? = null

    override fun bind(view: IDashboardActivityView) {
        mView = view
        mView?.initComponents()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_sleep_record -> {
                mCurrentFragment = WeakReference(RecordsFragment.newInstance())
                mCurrentFragment?.get()?.let {
                    mView?.changeFragment(mCurrentFragment!!.get()!!, "recordsFragment")
                }
                mView?.setTitle(BaseApplication.instance.getString(R.string.record_title))


            }
            R.id.navigation_size -> {
                mCurrentFragment = WeakReference(SizeRecordFragment.newInstance())
                mCurrentFragment?.get()?.let {
                    mView?.changeFragment(mCurrentFragment!!.get()!!, "sizeRecordFragment")
                }
                mView?.setTitle(BaseApplication.instance.getString(R.string.size_records_title))

            }
            R.id.navigation_profile -> {
                mCurrentFragment = WeakReference(BabyProfileFragment.newInstance())
                mCurrentFragment?.get()?.let {
                    mView?.changeFragment(mCurrentFragment!!.get()!!, "sleepRecordFragment")
                }
                mView?.setTitle(BaseApplication.instance.getString(R.string.baby_profile_title))
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCurrentFragment?.get()?.let {
            mCurrentFragment!!.get()!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun unBind() {
        mView = null
        mCurrentFragment?.clear()
    }
}