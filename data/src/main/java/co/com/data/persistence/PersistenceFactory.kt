package co.com.data.persistence

import android.arch.persistence.room.Room
import android.content.Context

/**
 * Created by oscarg798 on 12/20/17.
 */
class PersistenceFactory private constructor() {


    private lateinit var database: AppDatabase

    fun initDatabase(context: Context) {
        database = Room.databaseBuilder(context,
                AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6).build()

    }


    fun getDatabase(): AppDatabase {
        return database
    }


    private object Holder {
        val INSTANCE = PersistenceFactory()
    }

    companion object {
        val instance by lazy {
            Holder.INSTANCE
        }
    }


}