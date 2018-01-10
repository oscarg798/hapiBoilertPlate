package co.com.babyrecord.dashboard

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.com.babyrecord.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), IDashboardActivityView {

    private val mPresenter: IDashboardActivityPresenter = DashboardActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mPresenter.bind(this)
    }

    override fun initComponents() {
        setSupportActionBar(mToolbar)
        mBNVDashboard?.setOnNavigationItemSelectedListener(mPresenter)
        mBNVDashboard?.selectedItemId = R.id.navigation_sleep_record
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeFragment(fragment: Fragment, tag: String?) {
        fragmentManager?.let {
            mFLDashboard?.let {
                fragmentManager.beginTransaction()
                        .replace(R.id.mFLDashboard, fragment, tag)
                        .addToBackStack(tag)
                        .commitAllowingStateLoss()
            }
        }
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        fragmentManager?.let {
            if (fragmentManager.backStackEntryCount == 1) {
                finishAffinity()
                return
            }
        }
        super.onBackPressed()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode, resultCode, data)
    }
}
