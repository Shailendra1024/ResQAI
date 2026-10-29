package com.example.resqai.repository

import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.User
import com.example.resqai.network.SessionStore
import kotlinx.coroutines.delay

/**
 * Name, email and id come from the logged-in backend account.
 * Phone, blood group, allergies, conditions and emergency contacts are NOT stored by the backend yet,
 * so they stay local (in memory) until profile endpoints are added.
 */
class ProfileRepository {
    private val auth = AuthRepository()

    suspend fun getUserProfile(): User {
        auth.ensureDevSession()
        SessionStore.user?.let { u ->
            cached = cached.copy(id = u.id, name = u.name, email = u.email)
        }
        return cached
    }

    /** Local only for now: nothing is sent to the backend. */
    suspend fun updateUserProfile(user: User): User {
        delay(400)
        cached = user
        return user
    }

    private companion object {
        var cached: User = User(
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
}
