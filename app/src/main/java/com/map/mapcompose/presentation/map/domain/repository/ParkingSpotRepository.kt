package com.map.mapcompose.presentation.map.domain.repository

import com.map.mapcompose.presentation.map.domain.models.ParkingSpot
import kotlinx.coroutines.flow.Flow

interface ParkingSpotRepository {
    suspend fun insertParking(spot: ParkingSpot)
    suspend fun deleteParking(spot: ParkingSpot)
    fun getParking():Flow<List<ParkingSpot>>
}