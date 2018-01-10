package co.com.babyrecord.add_size_record

import co.com.babyrecord.IBaseView
import co.com.core.models.SizeRecord

/**
 * Created by oscarg798 on 12/22/17.
 */
interface IAddSizeRecordActivityView : IBaseView {

    fun showDateError()

    fun onSizeRecordSave(sizeRecord: SizeRecord)

    fun showDate(date: String)


}