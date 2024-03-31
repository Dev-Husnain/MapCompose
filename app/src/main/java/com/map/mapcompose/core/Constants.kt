package com.map.mapcompose.core

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

object Constants {
    fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun calculateDistance(latLngList: List<LatLng>): Double {
        var totalDistance = 0.0

        for (i in 0 until latLngList.size - 1) {
            totalDistance += SphericalUtil.computeDistanceBetween(latLngList[i], latLngList[i + 1])
        }

        return (totalDistance * 0.001)
    }

    fun calculateSurfaceArea(latLngList: List<LatLng>): Double {
        if (latLngList.size < 3) {
            return 0.0
        }
        return SphericalUtil.computeArea(latLngList)
    }

    fun formattedValue(value: Double) = String.format("%.2f", value)
}