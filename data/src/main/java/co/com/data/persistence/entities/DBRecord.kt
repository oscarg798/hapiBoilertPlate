package co.com.data.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by oscarg798 on 12/20/17.
 */
@Entity(tableName = "record")
data class DBRecord(@PrimaryKey val uuid: String,
                    val startTime: Long,
                    val endTime: Long? = null,
                    val type: String)