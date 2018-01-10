package co.com.babyrecord.records

import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.com.babyrecord.DATE_PICKER_DIALOG_TAG
import co.com.babyrecord.R
import co.com.babyrecord.TIME_PICKER_DIALOG_TAG
import co.com.babyrecord.Utils
import co.com.core.models.Record
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_sleep_record.*
import java.util.*
import kotlin.collections.ArrayList

class RecordsFragment : Fragment(), IRecordsFragmentView {

    private val mPresenter: IRecordsFragmentPresenter = RecordsFragmentPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_sleep_record, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.bind(this)

    }


    private fun sendRefreshBroadcast() {
        activity?.let {
            Utils.instance.requestWidgetUpdate(activity)
        }

    }

    override fun initComponents() {
        activity?.let {
            mSRLDashboard?.setOnRefreshListener(mPresenter)
            mRVRecords?.setHasFixedSize(false)
            mRVRecords?.layoutManager = LinearLayoutManager(activity)
            mRVRecords?.adapter = RecordsRecyclerViewAdapter(ArrayList(), mPresenter)

            mFABCreateSleepRecord?.setOnClickListener { v ->
                mPresenter.creteRecord(v.id)
                mFAMRecord.collapse()
            }

            mFABCreateMedicineRecord?.setOnClickListener { v ->
                mPresenter.creteRecord(v.id)
                mFAMRecord.collapse()
            }

            mFABCreateFeedRecord.setOnClickListener { v ->
                mPresenter.creteRecord(v.id)
                mFAMRecord.collapse()
            }

            mTVRecordsDate?.setOnClickListener {
                mPresenter.changeDate()
            }

            mIVNext?.setOnClickListener { v -> mPresenter.changeDateIVPresed(v.id) }
            mIVPrevious?.setOnClickListener { v -> mPresenter.changeDateIVPresed(v.id) }
            mIVSleepTab?.setOnClickListener { v ->
                mPresenter.onTabClickListener(v.id)
                changeTabIcons(v.id)
            }
            mIVFeedTab?.setOnClickListener { v ->
                mPresenter.onTabClickListener(v.id)
                changeTabIcons(v.id)
            }
            mIVMedicineTab?.setOnClickListener { v ->
                mPresenter.onTabClickListener(v.id)
                changeTabIcons(v.id)
            }
        }
    }

    private fun changeTabIcons(selectedTabID: Int) {
        when(selectedTabID){
            R.id.mIVSleepTab->{
                mIVSleepTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_selected_left_tab)
                mIVFeedTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_no_selected_tab)
                mIVMedicineTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_no_selected_tab)
            }
            R.id.mIVFeedTab->{
                mIVSleepTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_no_selected_tab)
                mIVFeedTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_selected_tab)
                mIVMedicineTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_no_selected_tab)
            }
            else->{
                mIVSleepTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_no_selected_tab)
                mIVFeedTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_no_selected_tab)
                mIVMedicineTab?.background = ContextCompat.getDrawable(activity, R.drawable.bg_selected_tab)
            }
        }
    }

    override fun showDateDialog(minDate: Calendar, maxDate: Calendar, listener: DatePickerDialog.OnDateSetListener) {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        )
        dpd.maxDate = maxDate
        dpd.minDate = minDate

        dpd.show(fragmentManager, DATE_PICKER_DIALOG_TAG)
    }

    override fun showTimeDialog(listener: TimePickerDialog.OnTimeSetListener) {
        val now = Calendar.getInstance()
        val tpd = TimePickerDialog.newInstance(listener, now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true)
        tpd.show(fragmentManager, TIME_PICKER_DIALOG_TAG)
    }


    override fun showMessage(message: String) {
        mSRLDashboard?.let {
            Snackbar.make(mSRLDashboard!!, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun showProgressBar() {
        mSRLDashboard?.isRefreshing = true
    }

    override fun hideProgressBar() {
        mSRLDashboard?.isRefreshing = false
    }

    override fun showRecords(records: ArrayList<Record>) {
        mRVRecords?.adapter?.let {
            (mRVRecords.adapter as RecordsRecyclerViewAdapter).addRecord(records)
        }
        sendRefreshBroadcast()

    }

    override fun updateRecord(record: Record) {
        mRVRecords?.adapter?.let {
            (mRVRecords.adapter as RecordsRecyclerViewAdapter).updateRecord(record)
        }
        sendRefreshBroadcast()

    }

    override fun addRecord(record: Record) {
        mRVRecords?.adapter?.let {
            (mRVRecords.adapter as RecordsRecyclerViewAdapter).addRecord(arrayListOf(record))
        }
        sendRefreshBroadcast()

    }

    override fun removeRecord(recordUuid: String) {
        mRVRecords?.adapter?.let {
            (mRVRecords.adapter as RecordsRecyclerViewAdapter).removeRecord(recordUuid)
        }
        sendRefreshBroadcast()

    }

    override fun showConfirmationAlertDialog(message: String, okButtonCallback: DialogInterface.OnClickListener) {
        activity?.let {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(message)
            builder.setPositiveButton(getString(R.string.accept_label), okButtonCallback)
            builder.setNegativeButton(getString(R.string.cancel_label)) { _, _ -> }
            builder.create().show()
        }

    }

    override fun showHideFabMenuCreateRecords(show: Boolean) {
        mFAMRecord?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun setRecordsDate(date: String) {
        mTVRecordsDate?.text = date
    }

    override fun hideIVNext() {
        mIVNext?.visibility = View.GONE
    }

    override fun showIVNext() {
        mIVNext?.visibility = View.VISIBLE
    }

    override fun clearRecords() {
        mRVRecords?.adapter?.let {
            (mRVRecords.adapter as RecordsRecyclerViewAdapter).clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unBind()
    }


    companion object {
        fun newInstance(): RecordsFragment = RecordsFragment()
    }
}
