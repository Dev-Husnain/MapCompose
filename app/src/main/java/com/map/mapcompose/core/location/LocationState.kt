package com.map.mapcompose.core.location

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class LocationState(
    val latLng: LatLng? = null,
    val error: String = "",
    val locality: String = "",
    val address: String = "",
    val location: Location? = null,
)
