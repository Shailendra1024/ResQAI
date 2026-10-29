package com.example.resqai.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register") suspend fun register(@Body body: RegisterBody): UserDto
    @POST("auth/login") suspend fun login(@Body body: LoginBody): LoginResponse

    @POST("sos/") suspend fun createSos(@Body body: SosCreateBody): SosDto
    @PATCH("sos/{id}/stage") suspend fun updateSosStage(@Path("id") id: String, @Body body: SosStageBody): SosDto

    @GET("alerts/") suspend fun getAlerts(@Query("severity") severity: String?): AlertListDto
    @GET("alerts/{id}") suspend fun getAlert(@Path("id") id: String): AlertDto

    @GET("shelters/") suspend fun getShelters(@Query("available_only") availableOnly: Boolean): ShelterListDto
}
