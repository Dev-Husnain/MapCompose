package com.map.mapcompose.presentation.map.domain.models

import java.util.Calendar

data class ParkingSpot(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    val id: Long = 0,
    val timeStamp: Long = Calendar.getInstance().timeInMillis,
)
