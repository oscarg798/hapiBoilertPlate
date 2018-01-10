package co.com.core.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by oscarg798 on 12/20/17.
 */
data class Record(val uuid: String,
                  val startTime: Long,
                  var endTime: Long?,
                  val type: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readLong(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(uuid)
        writeLong(startTime)
        writeValue(endTime)
        writeString(type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Record> = object : Parcelable.Creator<Record> {
            override fun createFromParcel(source: Parcel): Record = Record(source)
            override fun newArray(size: Int): Array<Record?> = arrayOfNulls(size)
        }
    }
}