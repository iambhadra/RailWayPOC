package com.bhadra.railwaybooking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = arrayOf(TrainDBData::class) , version = 1, exportSchema = false)
abstract class TrainDataBase :RoomDatabase() {
    abstract fun TrainDao():TrainDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TrainDataBase? = null

        fun getDatabase(context: Context): TrainDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrainDataBase::class.java,
                    "train_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}