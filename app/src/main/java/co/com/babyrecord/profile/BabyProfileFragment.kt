package co.com.babyrecord.profile


import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.com.babyrecord.BABY_KEY
import co.com.babyrecord.CREATE_OR_EDIT_BABY_REQUEST_CODE

import co.com.babyrecord.R
import co.com.babyrecord.Utils
import co.com.babyrecord.create_or_edit_baby.CreateOrEditBabyActivity
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * A simple [Fragment] subclass.
 */
class BabyProfileFragment : Fragment(), IBabyProfileFragmentView {


    private val mPresenter: IBabyProfileFragmentPresenter = BabyProfileFragmentPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unBind()
    }

    override fun initComponents() {
        mSRLProfile?.isEnabled = false
        mFabCreateBaby?.setOnClickListener {
            activity?.let {

                val intent = Intent(activity,
                        CreateOrEditBabyActivity::class.java)
                val baby = mPresenter.getBaby()
                baby?.let {
                    intent.putExtra(BABY_KEY, baby)
                }
                startActivityForResult(intent, CREATE_OR_EDIT_BABY_REQUEST_CODE)
            }

        }

    }

    override fun showProgressBar() {
        mSRLProfile?.isRefreshing = true

    }

    override fun hideProgressBar() {
        mSRLProfile?.isRefreshing = false
    }

    override fun showName(name: SpannableString) {
        mTVName?.text = name

    }

    override fun showBirthday(birthDay: SpannableString) {
        mTVBirthday?.text = birthDay
    }

    override fun showBabyAge(birthDay: Long) {
        mTVAge?.text = Utils.instance.getBabyAge(birthDay)
    }

    override fun showWeight(weight: SpannableString) {
        mTVWeigth?.text = weight
        mCVWeigth?.visibility = View.VISIBLE
    }

    override fun showHeight(height: SpannableString) {
        mTVHeight?.text = height
        mCVHeight?.visibility = View.VISIBLE
    }

    override fun hideWeight() {
        mCVWeigth?.visibility = View.GONE
    }

    override fun hideHeight() {
        mCVHeight?.visibility = View.GONE
    }

    override fun showNoBabyMessage() {
        mLLNoBabyMessage?.visibility = View.VISIBLE
        mFabCreateBaby?.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
        mCLBabyInfo?.visibility = View.GONE
    }

    override fun hideNoBabyMessage() {
        mLLNoBabyMessage?.visibility = View.GONE
        mFabCreateBaby?.setImageDrawable(resources.getDrawable(R.drawable.ic_edit))
        mCLBabyInfo?.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun getViewContext(): Context? {
        return activity
    }

    companion object {
        fun newInstance(): BabyProfileFragment {
            return BabyProfileFragment()
        }
    }

}// Required empty public constructor
