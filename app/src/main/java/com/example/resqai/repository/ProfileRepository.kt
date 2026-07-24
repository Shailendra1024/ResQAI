package com.example.resqai.repository

import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.User
import kotlinx.coroutines.delay

/**
 * Fake data source for the Profile feature.
 * Returns a single dummy logged-in user — no auth/backend involved.
 */
class ProfileRepository {

    suspend fun getUserProfile(): User {
        delay(300)
        return User(
            id = "u1",
            name = "Aditya Sharma",
            email = "aditya.sharma@example.com",
            phone = "+91 98765 43210",
            bloodGroup = "O+",
            allergies = "Penicillin",
            medicalConditions = "Mild asthma",
            emergencyContacts = listOf(
                EmergencyContact(id = "ec1", name = "Riya Sharma", phoneNumber = "+91 98765 11111", relation = "Sister"),
                EmergencyContact(id = "ec2", name = "Vikram Sharma", phoneNumber = "+91 98765 22222", relation = "Father")
            )
        )
    }

    /** Simulates saving profile edits — just echoes the updated user back. */
    suspend fun updateUserProfile(user: User): User {
        delay(400)
        return user
    }
}