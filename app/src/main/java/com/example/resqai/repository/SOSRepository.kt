package com.example.resqai.repository

import com.example.resqai.model.SOSRequest
import com.example.resqai.model.SOSStage
import com.example.resqai.network.ApiClient
import com.example.resqai.network.LocationProvider
import com.example.resqai.network.SessionStore
import com.example.resqai.network.SosCreateBody
import com.example.resqai.network.SosStageBody
import com.example.resqai.network.safeApi
import com.example.resqai.network.toModel
import kotlinx.coroutines.delay

/**
 * Sends the SOS to the backend (POST /sos/) and updates its stage (PATCH /sos/{id}/stage).
 * NOTE: the backend stores the request but does NOT dispatch real responders yet.
 */
class SOSRepository {
    private val auth = AuthRepository()

    /**
     * On failure this returns a request with stage IDLE and an empty id. It never pretends the
     * SOS was SENT when it wasn't. The ViewModel should treat id == "" as "failed" and show an error.
     */
    suspend fun sendSosRequest(): SOSRequest {
        if (!auth.ensureDevSession()) return failed()
        val dto = safeApi {
            ApiClient.service.createSos(
                SosCreateBody(LocationProvider.latitude, LocationProvider.longitude, "Emergency assistance required")
            )
        }.getOrNull() ?: return failed()

        SessionStore.currentSosId = dto.id
        val model = dto.toModel()
        SessionStore.currentSosStage = model.stage
        return model
    }

    /** Demo progression of the rescue stages. Returns the stage the backend confirmed. */
    suspend fun advanceToStage(stage: SOSStage): SOSStage {
        delay(1200) // keeps the demo pacing of the Rescue Status timeline
        val id = SessionStore.currentSosId ?: return SessionStore.currentSosStage
        // The backend only accepts these four stages.
        if (stage == SOSStage.IDLE || stage == SOSStage.CONFIRMING || stage == SOSStage.SENDING) return stage

        val dto = safeApi { ApiClient.service.updateSosStage(id, SosStageBody(stage.name)) }.getOrNull()
        val confirmed = dto?.toModel()?.stage ?: SessionStore.currentSosStage
        SessionStore.currentSosStage = confirmed
        return confirmed
    }

    private fun failed() = SOSRequest(id = "", stage = SOSStage.IDLE)
}
