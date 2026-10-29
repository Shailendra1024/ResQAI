package com.example.resqai.network

import com.example.resqai.model.Alert
import com.example.resqai.model.AlertSeverity
import com.example.resqai.model.AlertStatus
import com.example.resqai.model.SOSRequest
import com.example.resqai.model.SOSStage
import com.example.resqai.model.Shelter
import java.text.SimpleDateFormat
import java.util.Locale

internal fun AlertDto.toModel(): Alert = Alert(
    id = id,
    disasterType = title,
    severity = when (severity.lowercase()) {
        "critical", "high" -> AlertSeverity.HIGH
        "medium" -> AlertSeverity.MEDIUM
        else -> AlertSeverity.LOW
    },
    location = location,
    time = createdAt.toRelativeTime(),
    // The backend has no status / affected-area / recommended-actions fields yet.
    status = AlertStatus.ACTIVE,
    description = description,
    affectedArea = location,
    recommendedActions = emptyList()
)

internal fun ShelterDto.toModel(): Shelter = Shelter(
    id = id,
    name = name,
    distanceKm = Math.round(LocationProvider.distanceKm(latitude, longitude) * 10) / 10.0,
    capacity = capacity,
    currentOccupancy = capacity - availableSpaces,
    address = address
)

internal fun SosDto.toModel(): SOSRequest = SOSRequest(
    id = id,
    stage = runCatching { SOSStage.valueOf(stage) }.getOrDefault(SOSStage.SENT),
    sentAt = createdAt.toRelativeTime(),
    responderName = responderName.orEmpty(),
    responderEta = responderEta.orEmpty()
)

/** "2026-10-25T01:41:32.289123+00:00" -> "10 min ago". Falls back to the raw string. */
internal fun String.toRelativeTime(): String {
    return try {
        val clean = replace(Regex("\\.\\d+"), "")
        val then = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).parse(clean)?.time
            ?: return this
        val mins = (System.currentTimeMillis() - then) / 60_000
        when {
            mins < 1 -> "Just now"
            mins < 60 -> "$mins min ago"
            mins < 1440 -> "${mins / 60} hr ago"
            else -> "${mins / 1440} days ago"
        }
    } catch (e: Exception) {
        this
    }
}
