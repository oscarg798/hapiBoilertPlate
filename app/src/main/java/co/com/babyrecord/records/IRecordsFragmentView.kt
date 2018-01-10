package co.com.babyrecord.records

import android.content.DialogInterface
import co.com.babyrecord.IBaseView
import co.com.core.models.Record
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*

/**
 * Created by oscarg798 on 12/20/17.
 */
interface IRecordsFragmentView : IBaseView {

    fun showRecords(records: ArrayList<Record>)

    fun updateRecord(record: Record)

    fun addRecord(record: Record)

    fun showConfirmationAlertDialog(message: String, okButtonCallback: DialogInterface.OnClickListener)

    fun removeRecord(recordUuid: String)

    fun setRecordsDate(date: String)

    fun clearRecords()

    fun showHideFabMenuCreateRecords(show: Boolean)

    fun hideIVNext()

    fun showIVNext()

    fun showDateDialog(minDate: Calendar, maxDate: Calendar,
                       listener: com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener)

    fun showTimeDialog(listener:TimePickerDialog.OnTimeSetListener)

    fun showMessage(message:String)



}