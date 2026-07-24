package com.example.resqai.repository

import com.example.resqai.model.Alert
import com.example.resqai.model.AlertSeverity
import com.example.resqai.model.AlertStatus
import kotlinx.coroutines.delay

/**
 * Fake data source for the Alert History feature.
 * Returns a longer dummy list spanning resolved and past alerts —
 * reuses the same [Alert] model as the live Alerts feature.
 */
class HistoryRepository {

    private val dummyHistory = listOf(
        Alert(id = "h1", disasterType = "Flood Warning", severity = AlertSeverity.HIGH, location = "Riverside District", time = "2 days ago", status = AlertStatus.RESOLVED),
        Alert(id = "h2", disasterType = "Heavy Rainfall", severity = AlertSeverity.MEDIUM, location = "Downtown Area", time = "3 days ago", status = AlertStatus.RESOLVED),
        Alert(id = "h3", disasterType = "Heat Wave Advisory", severity = AlertSeverity.LOW, location = "City Wide", time = "5 days ago", status = AlertStatus.RESOLVED),
        Alert(id = "h4", disasterType = "Earthquake Tremor", severity = AlertSeverity.MEDIUM, location = "North Hills Region", time = "1 week ago", status = AlertStatus.RESOLVED),
        Alert(id = "h5", disasterType = "Cyclone Watch", severity = AlertSeverity.HIGH, location = "Coastal Belt", time = "1 week ago", status = AlertStatus.RESOLVED),
        Alert(id = "h6", disasterType = "Landslide Risk", severity = AlertSeverity.HIGH, location = "Hillcrest Slopes", time = "2 weeks ago", status = AlertStatus.RESOLVED),
        Alert(id = "h7", disasterType = "Thunderstorm Warning", severity = AlertSeverity.MEDIUM, location = "Downtown Area", time = "2 weeks ago", status = AlertStatus.RESOLVED),
        Alert(id = "h8", disasterType = "Air Quality Advisory", severity = AlertSeverity.LOW, location = "Industrial Zone", time = "3 weeks ago", status = AlertStatus.RESOLVED),
        Alert(id = "h9", disasterType = "Wildfire Smoke Alert", severity = AlertSeverity.MEDIUM, location = "North Hills Region", time = "1 month ago", status = AlertStatus.RESOLVED),
        Alert(id = "h10", disasterType = "Flash Flood Warning", severity = AlertSeverity.HIGH, location = "Riverside District", time = "1 month ago", status = AlertStatus.RESOLVED)
    )

    suspend fun getHistory(): List<Alert> {
        delay(300)
        return dummyHistory
    }
}