package com.spoonart.feature_textscanner.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import java.util.Locale

internal object LocationUtils {

    fun getLocationName(context: Context, location: Location) : String{
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude,1)
        addresses?.let {
            val locName = it.firstOrNull()?.getAddressLine(0)
            return locName ?: "failed to get location"
        }
        return "failed to get location"
    }

}
