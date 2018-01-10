package co.com.babyrecord.size_record

import android.content.DialogInterface
import co.com.babyrecord.IBaseView
import co.com.core.models.SizeRecord

/**
 * Created by oscarg798 on 12/22/17.
 */
interface ISizeRecordFragmentView : IBaseView {


    fun showSizeRecords(sizeRecords: ArrayList<SizeRecord>)

    fun removeSizeRecord(sizeRecordUuid: String)

    fun showNoSizeRecordMessage()

    fun hideSizeRecordMessage()

    fun showConfirmationAlertDialog(message: String, okButtonCallback: DialogInterface.OnClickListener)

}