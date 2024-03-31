package com.map.mapcompose.presentation.map

import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.map.mapcompose.presentation.map.domain.models.ParkingSpot

data class MapState(
    val properties: MapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions(MapsJson.GreenMapStyle),
        isMyLocationEnabled = true,
        isTrafficEnabled = true
    ),
    val mapUiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        rotationGesturesEnabled = false, myLocationButtonEnabled = true
    ),
    val spotsList: List<ParkingSpot> = emptyList(),
)
