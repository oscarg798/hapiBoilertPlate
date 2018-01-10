package co.com.data.persistence.dao

import android.arch.persistence.room.*
import co.com.data.persistence.entities.DBSizeRecord

/**
 * Created by oscarg798 on 12/22/17.
 */
@Dao
interface SizeRecordDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbSizeRecord: DBSizeRecord)

    @Query("select * from size_record")
    fun get(): List<DBSizeRecord>

    @Delete
    fun delete(dbSizeRecord: DBSizeRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(dbSizeRecord: DBSizeRecord)

}