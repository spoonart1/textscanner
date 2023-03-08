package com.spoonart.feature_textscanner.utils

import android.util.TimeUtils
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

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
    ){
        override fun toString(): String {
            return "duration: ${durationInSeconds/60}min, distance: ${distanceInMeters/1000}km."
        }
    }

    fun prettyDuration(seconds:Long) : String{
        val hour = seconds / 60 / 60
        val minutes = (seconds / 60) % 60
        return String.format("%2d hr(s), %2d min(s)", hour, minutes)
    }

    fun prettyDistance(meter:Long) : String{
        val km = meter / 1000
        val m = meter % 1000
        return String.format("%2dkm, %2dm", km, m)
    }
}
