package com.example.resqai.model

/**
 * Represents the logged-in user's profile.
 * Used by the Profile screen; not persisted anywhere (dummy/in-memory only).
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val bloodGroup: String = "",
    val allergies: String = "",
    val medicalConditions: String = "",
    val emergencyContacts: List<EmergencyContact> = emptyList()
)