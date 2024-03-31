package com.map.mapcompose.presentation.map

import com.google.android.gms.maps.model.LatLng
import com.map.mapcompose.presentation.map.domain.models.ParkingSpot

sealed class MapEvents {
    data class ChangeMapStyle(val styles: MapStyles) :MapEvents()
    data class OnMapLongClick(val latLng: LatLng):MapEvents()
    data class OnInfoWindowLongClick(val spot: ParkingSpot):MapEvents()
}