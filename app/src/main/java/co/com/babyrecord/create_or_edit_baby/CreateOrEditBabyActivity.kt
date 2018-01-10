package co.com.babyrecord.create_or_edit_baby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import co.com.babyrecord.BABY_KEY
import co.com.babyrecord.DATE_PICKER_DIALOG_TAG
import co.com.babyrecord.R
import co.com.core.models.Baby
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_create_or_edit_baby.*
import java.util.*

class CreateOrEditBabyActivity : AppCompatActivity(), ICreateOrEditBabyActivityView {

    private val mPresenter = CreateOrEditBabyActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_or_edit_baby)
        mPresenter.bind(this)
        mPresenter.bindArguments(intent.extras)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == R.id.action_save) {
                val babyName = mETName?.text.toString()
                if (babyName.length > 5) {
                    mPresenter.saveBaby(babyName)
                } else {
                    mETName?.error = "You should enter a Baby's name s"
                }

            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater?.inflate(R.menu.create_or_edit_baby_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun initComponents() {
        mSRLCreateOrEditBaby?.isEnabled = false
        mETBirthday?.isFocusable = false
        mETBirthday?.setOnClickListener {
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

    override fun showProgressBar() {
        mSRLCreateOrEditBaby?.isRefreshing = true
    }

    override fun hideProgressBar() {
        mSRLCreateOrEditBaby?.isRefreshing = false

    }

    override fun showName(name: String) {
        mETName?.setText(name)

    }

    override fun showBirthday(birthDay: String) {
        mETBirthday?.setText(birthDay)

    }

    override fun showBirthdayError() {
        mETBirthday?.error = "You should enter a valid birthday for your baby"
    }


    override fun onBabySaved(baby: Baby) {
        val intent = Intent()
        intent.putExtra(BABY_KEY, baby)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }
}
