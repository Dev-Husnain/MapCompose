package com.map.mapcompose.core.location

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.map.mapcompose.core.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    init {
        getLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { mLoc ->
                val latLng = LatLng(mLoc.latitude, mLoc.longitude)
                getAddressFromLatLng(latLng.latitude, latLng.longitude)
                _state.update {
                    it.copy(latLng = latLng, location = mLoc)
                }

            } ?: let {
                _state.update {
                    it.copy(error = "Location not found")
                }
            }
        }.addOnFailureListener { exe ->
            _state.update {
                it.copy(error = exe.message.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastCurrentLocation() {
        locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location: Location? ->
            location?.let { mLoc ->
                val latLng = LatLng(mLoc.latitude, mLoc.longitude)
                _state.update {
                    it.copy(latLng = latLng, location = mLoc)
                }
            } ?: let {
                _state.update {
                    it.copy(error = "Location not found")
                }
            }
        }.addOnFailureListener { exe ->
            _state.update {
                it.copy(error = exe.message.toString())
            }
        }
    }

     fun getAddressFromLatLng(lat: Double, long: Double,isFromClick:Boolean=false) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(lat, long, 1) { p0: List<Address>? ->
                        p0?.let { list ->
                            val address = list[0].getAddressLine(0)
                            val locality = list[0].locality
                            _state.update {
                                it.copy(address = address, locality = locality)
                            }
                            if (isFromClick){
                                _state.update {
                                    it.copy(latLng = LatLng(lat,long))
                                }
                            }
                        }

                    }
                } else {
                    val addresses = geocoder.getFromLocation(lat, long, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0].getAddressLine(0)
                        val locality = addresses[0].locality
                        _state.update {
                            it.copy(address = address, locality = locality)
                        }
                    } else {
                        ResponseState.Error("Address not found")
                    }
                }
            } catch (_: Exception) {
            }
        }
}
