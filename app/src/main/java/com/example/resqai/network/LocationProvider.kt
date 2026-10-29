package com.example.resqai.network

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * PLACEHOLDER for the user's location. These fixed coordinates are used for SOS requests and
 * shelter distances until real GPS (FusedLocationProviderClient + location permission) is added.
 * Update latitude/longitude from the GPS callback and everything else follows.
 */
object LocationProvider {
    @Volatile var latitude: Double = 26.8467
    @Volatile var longitude: Double = 80.9462

    fun distanceKm(lat: Double, lng: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat - latitude)
        val dLng = Math.toRadians(lng - longitude)
        val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(latitude)) * cos(Math.toRadians(lat)) * sin(dLng / 2).pow(2)
        return 2 * r * asin(sqrt(a))
    }
}
