package co.com.data.persistence.dao

import android.arch.persistence.room.*
import co.com.data.persistence.entities.DBBaby

/**
 * Created by oscarg798 on 12/21/17.
 */
@Dao
interface BabyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbBaby: DBBaby)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(dbBaby: DBBaby)

    @Delete
    fun delete(dbBaby: DBBaby)

    @Query("select * from baby limit 1")
    fun get(): DBBaby
}