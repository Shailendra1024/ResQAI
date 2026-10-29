package com.example.resqai.network

import com.google.gson.annotations.SerializedName

// ---- Auth ----
data class RegisterBody(val name: String, val email: String, val password: String)
data class LoginBody(val email: String, val password: String)
data class UserDto(val id: String, val name: String, val email: String)
data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in_minutes") val expiresInMinutes: Int,
    val user: UserDto
)

// ---- SOS ----
data class SosCreateBody(val latitude: Double, val longitude: Double, val message: String)
data class SosStageBody(val stage: String)
data class SosDto(
    val id: String,
    @SerializedName("user_id") val userId: String,
    val latitude: Double,
    val longitude: Double,
    val message: String,
    val stage: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("responder_name") val responderName: String?,
    @SerializedName("responder_eta") val responderEta: String?,
    @SerializedName("dispatch_status") val dispatchStatus: String?
)

// ---- Alerts ----
data class AlertDto(
    val id: String,
    val title: String,
    val description: String,
    val severity: String,
    val location: String,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName("created_at") val createdAt: String,
    val source: String?
)
data class AlertListDto(val count: Int, val items: List<AlertDto>)

// ---- Shelters ----
data class ShelterDto(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int,
    @SerializedName("available_spaces") val availableSpaces: Int,
    val contact: String?
)
data class ShelterListDto(val count: Int, val items: List<ShelterDto>)
