package co.com.data.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by oscarg798 on 12/21/17.
 */
@Entity(tableName = "baby")
data class DBBaby(@PrimaryKey val uuid: String = UUID.randomUUID().toString(),
                  val name: String,
                  val birthDate: Long)