package com.map.mapcompose.presentation.map

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HeartBroken
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.map.mapcompose.core.Constants.calculateSurfaceArea
import com.map.mapcompose.core.Constants.formattedValue
import com.map.mapcompose.core.location.LocationsViewModel

@Composable
fun MapScreen(
    mapsViewModel: MapsViewModel = viewModel(),
    locationsViewModel: LocationsViewModel = viewModel(),
) {

    val state by mapsViewModel.state.collectAsState()
    val locationState by locationsViewModel.state.collectAsState()
    var isBlueDark by rememberSaveable {
        mutableStateOf(true)
    }
    var showMap by rememberSaveable { mutableStateOf(false) }
    var mLatLng by rememberSaveable { mutableStateOf<LatLng?>(null) }
    val markersList = remember {
        mutableStateListOf(mLatLng)
    }


    val cameraPositionState = rememberCameraPositionState()

    mLatLng = locationState.latLng
    if (mLatLng != null) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(mLatLng!!, 12f)
        showMap = true
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (showMap) {
            Scaffold(floatingActionButton = {
                FloatingActionButton(onClick = {
                    isBlueDark = !isBlueDark
                    mapsViewModel.onEvent(MapEvents.ChangeMapStyle(if (isBlueDark) MapStyles.GREEN_DARK else MapStyles.BLUE_DARK))
                }) {
                    Icon(
                        imageVector = Icons.Rounded.HeartBroken,
                        contentDescription = "Map Styles"
                    )
                }
            }) {
                GoogleMap(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    properties = state.properties,
                    uiSettings = state.mapUiSettings,
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLong ->
                        locationsViewModel.getAddressFromLatLng(
                            latLong.latitude,
                            latLong.longitude,
                            true
                        )
                        markersList.add(latLong)
                    },
                    onMapLongClick = { latLng ->
                        mapsViewModel.onEvent(MapEvents.OnMapLongClick(latLng))
                    }
                ) {
                    markersList.filterNotNull().forEach { latLng ->
                        Marker(
                            state = rememberMarkerState(position = latLng)
                        )

                    }
                    if (markersList.filterNotNull().isNotEmpty()) {
                        Polygon(
                            points = markersList.filterNotNull(),
                            fillColor = Color.Green,
                            strokeColor = Color.Red,
                            strokeWidth = 2F
                        )
                        val area="Total surface area= ${formattedValue(calculateSurfaceArea(markersList.filterNotNull()))} sq. meter"
                        Log.d("cvv", "MapScreen Area=: $area")
                        Log.d("cvv", "MapScreen Address=: ${locationState.address}")
                    }


                    state.spotsList.forEach { spot ->
                        val markerState = rememberMarkerState(
                            key = spot.id.toString(),
                            position = LatLng(spot.lat, spot.lng)
                        )
                        Marker(
                            state = markerState,
                            onInfoWindowLongClick = {
                                mapsViewModel.onEvent(MapEvents.OnInfoWindowLongClick(spot))
                            },
                            title = "Marker on Point ${spot.lat}, ${spot.lng}",
                            snippet = "You can delete it on long click",
                            onClick = { mMarker ->
                                mMarker.showInfoWindow()
                                true
                            },
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                        )
                    }
                }
            }
        } else {
            AnimatedVisibility(visible = locationState.error.isBlank()) {
                CircularProgressIndicator(
                    modifier = Modifier.wrapContentHeight(),
                    color = Color.Blue
                )
            }
            AnimatedVisibility(visible = locationState.error.isNotBlank()) {
                Text(text = locationState.error, color = Color.Black, fontSize = 15.sp)
            }
        }
    }


}