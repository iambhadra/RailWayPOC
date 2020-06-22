package com.bhadra.railwaybooking.database

import androidx.room.*

@Dao
interface TrainDao {

    @Query("SELECT * FROM TrainDBData")
    suspend fun getAllData():List<TrainDBData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrainData(vararg train: TrainDBData)

    @Query("DELETE FROM TrainDBData")
    suspend fun deleteData()
}