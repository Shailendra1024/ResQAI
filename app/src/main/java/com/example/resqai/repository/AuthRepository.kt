package com.example.resqai.repository

import com.example.resqai.network.ApiClient
import com.example.resqai.network.ApiConfig
import com.example.resqai.network.LoginBody
import com.example.resqai.network.RegisterBody
import com.example.resqai.network.SessionStore
import com.example.resqai.network.UserDto
import com.example.resqai.network.safeApi

// Talks to the auth endpoints. Use register()/login() from a real login screen.
class AuthRepository {

    suspend fun register(name: String, email: String, password: String): Result<UserDto> =
        safeApi { ApiClient.service.register(RegisterBody(name, email, password)) }

    suspend fun login(email: String, password: String): Result<UserDto> =
        safeApi {
            val res = ApiClient.service.login(LoginBody(email, password))
            SessionStore.token = res.accessToken
            SessionStore.user = res.user
            res.user
        }

    fun logout() = SessionStore.clear()

    /**
     * DEV ONLY (see ApiConfig.DEV_AUTO_LOGIN): makes sure a demo user is logged in so SOS works
     * before a login screen exists. The backend store is in-memory, so the demo user is
     * re-registered automatically after a backend restart.
     */
    suspend fun ensureDevSession(): Boolean {
        if (SessionStore.token != null) return true
        if (!ApiConfig.DEV_AUTO_LOGIN) return false
        if (login(ApiConfig.DEV_EMAIL, ApiConfig.DEV_PASSWORD).isSuccess) return true
        register(ApiConfig.DEV_NAME, ApiConfig.DEV_EMAIL, ApiConfig.DEV_PASSWORD)
        return login(ApiConfig.DEV_EMAIL, ApiConfig.DEV_PASSWORD).isSuccess
    }
}
