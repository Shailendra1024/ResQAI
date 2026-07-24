package com.example.resqai.model

/** Lifecycle stage of an SOS request, used to drive both SOS and Rescue Status screens. */
enum class SOSStage {
    IDLE,
    CONFIRMING,
    SENDING,
    SENT,
    RESPONDER_ASSIGNED,
    RESPONDER_EN_ROUTE,
    RESCUE_COMPLETED
}

/** Represents a single SOS emergency request and its current progress. */
data class SOSRequest(
    val id: String,
    val stage: SOSStage,
    val sentAt: String = "",
    val responderName: String = "",
    val responderEta: String = ""
)

/** A single step in the Rescue Status timeline. */
data class RescueTimelineStep(
    val label: String,
    val stage: SOSStage,
    val timestamp: String = "",
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)