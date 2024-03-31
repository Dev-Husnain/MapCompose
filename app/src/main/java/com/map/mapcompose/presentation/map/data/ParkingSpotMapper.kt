package com.map.mapcompose.presentation.map.data

import com.map.mapcompose.presentation.map.domain.models.ParkingSpot

fun ParkingSpotEntity.toParkingSpot():ParkingSpot{
    return ParkingSpot(lat,lng,id,timeStamp)
}
fun ParkingSpot.toParkingSpotEntity():ParkingSpotEntity{
    return ParkingSpotEntity(lat,lng,id,timeStamp)
}