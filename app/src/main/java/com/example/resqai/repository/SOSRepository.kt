package com.example.resqai.repository

import com.example.resqai.model.SOSRequest
import com.example.resqai.model.SOSStage
import kotlinx.coroutines.delay

/**
 * Fake data source simulating an SOS request lifecycle.
 * No real backend/dispatch system involved — pure delay-based simulation.
 */
class SOSRepository {

    /** Simulates the network call to submit an SOS request. */
    suspend fun sendSosRequest(): SOSRequest {
        delay(1800) // simulate "sending" progress animation
        return SOSRequest(
            id = "sos_${System.currentTimeMillis()}",
            stage = SOSStage.SENT,
            sentAt = "Just now",
            responderName = "Unit 12 - Rapid Response Team",
            responderEta = "8 min"
        )
    }

    /** Simulates the rescue progressing through subsequent stages over time. */
    suspend fun advanceToStage(stage: SOSStage): SOSStage {
        delay(1200)
        return stage
    }
}