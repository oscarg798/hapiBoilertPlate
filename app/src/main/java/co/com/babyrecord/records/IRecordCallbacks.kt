package co.com.babyrecord.records

import co.com.core.models.Record

/**
 * Created by oscarg798 on 12/20/17.
 */
interface IRecordCallbacks {

    fun finishRecord(record: Record)

    fun deleteRecord(record: Record)

    fun editRecordEndTime(record: Record)
}