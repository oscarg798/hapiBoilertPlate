package co.com.data.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by oscarg798 on 12/22/17.
 */
@Entity(tableName = "size_record")
data class DBSizeRecord(@PrimaryKey val uuid: String = UUID.randomUUID().toString(),
                        val height: Float,
                        val weight: Int,
                        val date: Long)