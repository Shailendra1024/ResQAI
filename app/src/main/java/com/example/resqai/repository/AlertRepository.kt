package com.example.resqai.repository

import com.example.resqai.model.Alert
import com.example.resqai.model.AlertSeverity
import com.example.resqai.model.AlertStatus
import kotlinx.coroutines.delay

/**
 * Fake data source for the Disaster Alerts feature.
 * Returns a richer dummy alert list (with description/affected area/
 * recommended actions) used by both the Alert List and Alert Details screens.
 */
class AlertRepository {

    private val dummyAlerts = listOf(
        Alert(
            id = "a1",
            disasterType = "Flood Warning",
            severity = AlertSeverity.HIGH,
            location = "Riverside District",
            time = "10 min ago",
            status = AlertStatus.ACTIVE,
            description = "Continuous heavy rainfall has caused the river level to rise sharply. " +
                    "Flash flooding is expected in low-lying areas within the next 6 hours. " +
                    "Residents are advised to move to higher ground immediately.",
            affectedArea = "Riverside District, Old Town, Market Street (approx. 4.2 km radius)",
            recommendedActions = listOf(
                "Move to higher ground immediately",
                "Avoid walking or driving through flood water",
                "Keep emergency kit and documents ready",
                "Follow evacuation routes shown on the Safe Route screen"
            )
        ),
        Alert(
            id = "a2",
            disasterType = "Heavy Rainfall",
            severity = AlertSeverity.MEDIUM,
            location = "Downtown Area",
            time = "1 hr ago",
            status = AlertStatus.MONITORING,
            description = "Meteorological department forecasts sustained heavy rainfall over the " +
                    "next 12 hours. Localized waterlogging possible in low-drainage zones.",
            affectedArea = "Downtown Area, Central Business District",
            recommendedActions = listOf(
                "Avoid unnecessary travel",
                "Keep vehicles away from underpasses",
                "Monitor local news for updates"
            )
        ),
        Alert(
            id = "a3",
            disasterType = "Heat Wave Advisory",
            severity = AlertSeverity.LOW,
            location = "City Wide",
            time = "3 hr ago",
            status = AlertStatus.MONITORING,
            description = "Temperatures are expected to remain 4-6°C above seasonal average for the " +
                    "next 3 days. Risk of heat exhaustion for outdoor workers and elderly residents.",
            affectedArea = "City Wide",
            recommendedActions = listOf(
                "Stay hydrated and avoid direct sun 12PM-3PM",
                "Check on elderly neighbors and relatives",
                "Wear light, breathable clothing outdoors"
            )
        ),
        Alert(
            id = "a4",
            disasterType = "Earthquake Tremor",
            severity = AlertSeverity.MEDIUM,
            location = "North Hills Region",
            time = "5 hr ago",
            status = AlertStatus.RESOLVED,
            description = "A magnitude 4.2 tremor was recorded. No major structural damage reported. " +
                    "Aftershocks are possible but unlikely to cause significant harm.",
            affectedArea = "North Hills Region, Hillcrest, Pine Valley",
            recommendedActions = listOf(
                "Inspect your home for visible cracks",
                "Be prepared for possible minor aftershocks",
                "Report any gas leaks to authorities immediately"
            )
        ),
        Alert(
            id = "a5",
            disasterType = "Cyclone Watch",
            severity = AlertSeverity.HIGH,
            location = "Coastal Belt",
            time = "8 hr ago",
            status = AlertStatus.ACTIVE,
            description = "A tropical cyclone system is forming off the coast and may make landfall " +
                    "within 48 hours. Wind speeds could exceed 100 km/h near the coastline.",
            affectedArea = "Coastal Belt, Harbor District, Fisherman's Wharf",
            recommendedActions = listOf(
                "Secure loose outdoor items",
                "Stock up on food, water, and batteries",
                "Follow official evacuation orders if issued",
                "Charge all mobile devices in advance"
            )
        )
    )

    suspend fun getAlerts(): List<Alert> {
        delay(300)
        return dummyAlerts
    }

    suspend fun getAlertById(id: String): Alert? {
        delay(150)
        return dummyAlerts.find { it.id == id }
    }
}