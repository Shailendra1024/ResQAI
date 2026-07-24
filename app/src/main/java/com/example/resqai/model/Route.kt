package com.example.resqai.model

/**
 * Represents a safe evacuation route from the user's current location
 * to a destination shelter.
 */
data class Route(
    val id: String,
    val originLabel: String,
    val destinationShelterName: String,
    val destinationAddress: String,
    val distanceKm: Double,
    val estimatedMinutes: Int,
    val safetyScore: Int, // 0-100, higher = safer
    val routeSummary: String = ""
) {
    /** Human-friendly safety tier derived from [safetyScore], used for color-coding. */
    val safetyTier: SafetyTier
        get() = when {
            safetyScore >= 75 -> SafetyTier.SAFE
            safetyScore >= 50 -> SafetyTier.MODERATE
            else -> SafetyTier.RISKY
        }
}

enum class SafetyTier { SAFE, MODERATE, RISKY }