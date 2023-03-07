package com.spoonart.feature_textscanner.utils

import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode

object DistanceUtils{

    private val destination = LatLng(-6.193798350458681, 106.82197691048982)
    private val apiKey = "AIzaSyAmjIJ8VeFgRG1OHaJ0InTW3RQphdVGcas"

    fun request(
        origin:LatLng,
        onResult: (DistanceData) -> Unit
    ){
        val apiContext = GeoApiContext.Builder()
            .apiKey(apiKey).build()

        val result = DirectionsApi.newRequest(apiContext)
            .origin(origin)
            .destination(destination)
            .mode(TravelMode.DRIVING)
            .await()

        val route = result.routes.first()
        val leg = route.legs.first()
        val distance = leg.distance.inMeters
        val duration = leg.duration.inSeconds
        onResult(DistanceData(duration, distance))
    }

    data class DistanceData(
        val durationInSeconds:Long,
        val distanceInMeters:Long
    )
}
