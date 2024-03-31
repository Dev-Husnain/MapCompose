package com.map.mapcompose.presentation.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingSpotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParking(spot: ParkingSpotEntity)

    @Delete
    suspend fun deleteParking(spot: ParkingSpotEntity)

    @Query("SELECT * FROM ParkingSpotEntity")
    fun getParkingSpots():Flow<List<ParkingSpotEntity>>

}