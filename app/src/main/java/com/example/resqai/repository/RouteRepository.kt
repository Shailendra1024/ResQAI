package com.example.resqai.repository

import com.example.resqai.model.Route
import kotlinx.coroutines.delay

/**
 * Fake data source for the Safe Route feature.
 * Returns a single dummy recommended route — in a real app this would
 * come from a routing/directions API weighted by live hazard data.
 */
class RouteRepository {

    suspend fun getSafeRoute(): Route {
        delay(300)
        return Route(
            id = "r1",
            originLabel = "Your Current Location",
            destinationShelterName = "Community Hall Shelter",
            destinationAddress = "42 Riverside Ave, near Central Park",
            distanceKm = 1.2,
            estimatedMinutes = 14,
            safetyScore = 82,
            routeSummary = "Avoids the flooded underpass on Main St. Route stays on elevated roads."
        )
    }
}