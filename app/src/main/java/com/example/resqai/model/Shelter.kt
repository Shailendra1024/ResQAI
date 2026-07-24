package com.example.resqai.model

/**
 * Represents a nearby emergency shelter.
 * Used by Home (nearby shelters card), Shelter screen, and Safe Route screen.
 */
data class Shelter(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val capacity: Int,
    val currentOccupancy: Int,
    val address: String = ""
) {
    val availableSpots: Int get() = capacity - currentOccupancy
    val isAvailable: Boolean get() = availableSpots > 0
}