package com.map.mapcompose.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.MapStyleOptions
import com.map.mapcompose.presentation.map.domain.models.ParkingSpot
import com.map.mapcompose.presentation.map.domain.repository.ParkingSpotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: ParkingSpotRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getParking().collectLatest { spots->
                _state.update {
                    it.copy(spotsList = spots)
                }
            }
        }
    }

    fun onEvent(events: MapEvents) {
        when (events) {
            is MapEvents.ChangeMapStyle -> {
                _state.update {
                    it.copy(
                        properties = state.value.properties.copy(
                            mapStyleOptions = MapStyleOptions(
                                getMapStyles(events.styles)
                            )
                        )
                    )
                }

            }

            is MapEvents.OnInfoWindowLongClick ->{
                viewModelScope.launch {
                    repository.deleteParking(events.spot)
                }
            }

            is MapEvents.OnMapLongClick -> {
                viewModelScope.launch {
                    repository.insertParking(
                        ParkingSpot(
                            events.latLng.latitude,
                            events.latLng.longitude
                        )
                    )
                }
            }
        }
    }

    private fun getMapStyles(styles: MapStyles): String {
        return when (styles) {
            MapStyles.GREEN_DARK -> MapsJson.GreenMapStyle
            MapStyles.BLUE_DARK -> MapsJson.BlueDark
        }
    }
}