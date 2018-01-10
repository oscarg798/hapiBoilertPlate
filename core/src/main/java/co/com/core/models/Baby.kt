package co.com.core.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by oscarg798 on 12/21/17.
 */
data class Baby(val name: String,
                val birthDate: Long,
                val weight: Int? = null,
                val height: Float? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readLong(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Float::class.java.classLoader) as Float?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeLong(birthDate)
        writeValue(weight)
        writeValue(height)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Baby> = object : Parcelable.Creator<Baby> {
            override fun createFromParcel(source: Parcel): Baby = Baby(source)
            override fun newArray(size: Int): Array<Baby?> = arrayOfNulls(size)
        }
    }
}