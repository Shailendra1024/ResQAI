package com.example.resqai.repository

import com.example.resqai.model.Shelter
import kotlinx.coroutines.delay

/**
 * Fake data source for the Shelter feature.
 * Returns an expanded dummy shelter list — no real backend/database involved.
 */
class ShelterRepository {

    private val dummyShelters = listOf(
        Shelter(id = "s1", name = "Community Hall Shelter", distanceKm = 1.2, capacity = 200, currentOccupancy = 120, address = "42 Riverside Ave"),
        Shelter(id = "s2", name = "St. Mary's School Shelter", distanceKm = 2.5, capacity = 150, currentOccupancy = 150, address = "18 Church Road"),
        Shelter(id = "s3", name = "Riverside Sports Complex", distanceKm = 3.8, capacity = 300, currentOccupancy = 90, address = "7 Stadium Lane"),
        Shelter(id = "s4", name = "North Hills Community Center", distanceKm = 4.6, capacity = 180, currentOccupancy = 60, address = "101 Hillcrest Blvd"),
        Shelter(id = "s5", name = "Central Library Shelter", distanceKm = 5.1, capacity = 100, currentOccupancy = 100, address = "5 Main Street"),
        Shelter(id = "s6", name = "Downtown Fire Station Annex", distanceKm = 6.3, capacity = 80, currentOccupancy = 25, address = "220 5th Avenue")
    )

    suspend fun getShelters(): List<Shelter> {
        delay(300)
        return dummyShelters
    }

    suspend fun searchShelters(query: String): List<Shelter> {
        delay(150)
        if (query.isBlank()) return dummyShelters
        return dummyShelters.filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }
    }
}