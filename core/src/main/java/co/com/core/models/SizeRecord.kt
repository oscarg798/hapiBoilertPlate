package co.com.core.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by oscarg798 on 12/22/17.
 */
data class SizeRecord(val uuid: String = UUID.randomUUID().toString(),
                      val height: Float,
                      val weight: Int,
                      val date: Long) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeFloat(height)
        parcel.writeInt(weight)
        parcel.writeLong(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SizeRecord> {
        override fun createFromParcel(parcel: Parcel): SizeRecord {
            return SizeRecord(parcel)
        }

        override fun newArray(size: Int): Array<SizeRecord?> {
            return arrayOfNulls(size)
        }
    }
}