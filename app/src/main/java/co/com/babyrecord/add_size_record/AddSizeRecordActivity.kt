package co.com.babyrecord.add_size_record

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import co.com.babyrecord.DATE_PICKER_DIALOG_TAG
import co.com.babyrecord.R
import co.com.babyrecord.SIZE_RECORD_KEY
import co.com.core.models.SizeRecord
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_add_size_record.*
import java.util.*

class AddSizeRecordActivity : AppCompatActivity(),
        IAddSizeRecordActivityView {


    private val mPresenter: IAddSizeRecordActivityPresenter = AddSizeRecordActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_size_record)
        mPresenter.bind(this)

    }

    override fun initComponents() {
        mSRLAddSizeRecord?.isEnabled = false
        mETDate?.isFocusable = false
        mETDate?.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog.newInstance(
                    mPresenter,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show(fragmentManager, DATE_PICKER_DIALOG_TAG)
        }
    }

    override fun showDateError() {
        mETDate?.error = "You should enter when was the measurement made"
    }

    override fun showProgressBar() {
        mSRLAddSizeRecord?.isRefreshing = true
    }

    override fun hideProgressBar() {
        mSRLAddSizeRecord?.isRefreshing = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == R.id.action_save) {
                val height = mETHeight?.text.toString()
                val weight = mETWeight?.text.toString()
                when {
                    TextUtils.isEmpty(height) -> mETHeight?.error = "You must enter the height on cm"
                    TextUtils.isEmpty(weight) -> mETWeight?.error = "You must enter the weight on pounds"
                    else -> mPresenter.saveSizeRecord(height.toFloat(), weight.toInt())
                }


            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showDate(date: String) {
        mETDate?.setText(date)
    }

    override fun onSizeRecordSave(sizeRecord: SizeRecord) {
        val intent = Intent()
        intent.putExtra(SIZE_RECORD_KEY, sizeRecord)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater?.inflate(R.menu.create_or_edit_baby_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

}
