package com.map.mapcompose.presentation.map.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
@Keep
@Entity
data class ParkingSpotEntity(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timeStamp:Long = Calendar.getInstance().timeInMillis
)
