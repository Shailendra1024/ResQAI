package com.example.resqai.model

/**
 * Represents an emergency contact — either a service (Police, Fire, Hospital)
 * or a personal contact set up in the user's Profile.
 */
data class EmergencyContact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val relation: String = "" // e.g. "Family", "Friend", or empty for services
)