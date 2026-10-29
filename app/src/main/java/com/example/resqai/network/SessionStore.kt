package com.example.resqai.network

import com.example.resqai.model.SOSStage

/**
 * In-memory session (lost when the app process dies).
 * TODO: persist the token with DataStore / EncryptedSharedPreferences.
 */
object SessionStore {
    @Volatile var token: String? = null
    @Volatile var user: UserDto? = null
    @Volatile var currentSosId: String? = null
    @Volatile var currentSosStage: SOSStage = SOSStage.IDLE

    fun clear() {
        token = null
        user = null
        currentSosId = null
        currentSosStage = SOSStage.IDLE
    }
}
