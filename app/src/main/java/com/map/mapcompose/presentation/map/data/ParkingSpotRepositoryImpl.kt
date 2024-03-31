package com.map.mapcompose.presentation.map.data

import com.map.mapcompose.presentation.map.domain.models.ParkingSpot
import com.map.mapcompose.presentation.map.domain.repository.ParkingSpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ParkingSpotRepositoryImpl(private val dao: ParkingSpotDao) : ParkingSpotRepository {
    override suspend fun insertParking(spot: ParkingSpot) {
        dao.insertParking(spot.toParkingSpotEntity())
    }

    override suspend fun deleteParking(spot: ParkingSpot) {
        dao.deleteParking(spot.toParkingSpotEntity())
    }

    override fun getParking(): Flow<List<ParkingSpot>> {
        return dao.getParkingSpots().map { list ->
            list.map {
                it.toParkingSpot()
            }
        }
    }
}