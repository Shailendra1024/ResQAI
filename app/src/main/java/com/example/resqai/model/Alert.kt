package com.example.resqai.model

/** Severity level used for color-coding alerts across the app. */
enum class AlertSeverity { HIGH, MEDIUM, LOW }

/** Current lifecycle status of a disaster alert. */
enum class AlertStatus { ACTIVE, MONITORING, RESOLVED }

/**
 * Represents a single disaster alert.
 * Used by Home (latest alerts), Alert list, Alert details, and History screens.
 */
data class Alert(
    val id: String,
    val disasterType: String,
    val severity: AlertSeverity,
    val location: String,
    val time: String,
    val status: AlertStatus,
    val description: String = "",
    val affectedArea: String = "",
    val recommendedActions: List<String> = emptyList()
)