package co.com.data.persistence.dao

import android.arch.persistence.room.*
import co.com.data.persistence.entities.DBRecord

/**
 * Created by oscarg798 on 12/20/17.
 */
@Dao
interface RecordDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbRecord: DBRecord)

    @Query("select * from record")
    fun getRecords(): List<DBRecord>

    @Delete
    fun delete(dbRecord: DBRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(dbRecord: DBRecord)

    @Query("select min(startTime) from record")
    fun getRecordsMinDate():Long

    @Query("select max(startTime) from record")
    fun getRecordsMaxDate():Long

}