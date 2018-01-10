package co.com.babyrecord.add_size_record

import co.com.babyrecord.IBasePresenter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

/**
 * Created by oscarg798 on 12/22/17.
 */
interface IAddSizeRecordActivityPresenter : IBasePresenter<IAddSizeRecordActivityView>,
        DatePickerDialog.OnDateSetListener {

    fun saveSizeRecord(height: Float, weight: Int)
}