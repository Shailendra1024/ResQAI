package com.example.resqai.repository

import com.example.resqai.model.Alert
import com.example.resqai.model.AlertSeverity
import com.example.resqai.model.AlertStatus
import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.Shelter
import kotlinx.coroutines.delay

/**
 * Fake data source for the Home Dashboard.
 * Simulates a network/database call with a short delay and returns
 * hardcoded dummy data — no real backend involved.
 */
class HomeRepository {

    suspend fun getRiskStatus(): String {
        delay(300)
        return "Moderate Risk"
    }

    suspend fun getWeather(): WeatherInfo {
        delay(300)
        return WeatherInfo(
            temperatureC = 29,
            condition = "Partly Cloudy",
            humidity = 68,
            windKmh = 14
        )
    }

    suspend fun getNearbyShelters(): List<Shelter> {
        delay(300)
        return listOf(
            Shelter(id = "s1", name = "Community Hall Shelter", distanceKm = 1.2, capacity = 200, currentOccupancy = 120),
            Shelter(id = "s2", name = "St. Mary's School Shelter", distanceKm = 2.5, capacity = 150, currentOccupancy = 150),
            Shelter(id = "s3", name = "Riverside Sports Complex", distanceKm = 3.8, capacity = 300, currentOccupancy = 90)
        )
    }

    suspend fun getEmergencyContacts(): List<EmergencyContact> {
        delay(300)
        return listOf(
            EmergencyContact(id = "c1", name = "Police", phoneNumber = "100"),
            EmergencyContact(id = "c2", name = "Fire Department", phoneNumber = "101"),
            EmergencyContact(id = "c3", name = "Ambulance", phoneNumber = "102")
        )
    }

    suspend fun getLatestAlerts(): List<Alert> {
        delay(300)
        return listOf(
            Alert(
                id = "a1",
                disasterType = "Flood Warning",
                severity = AlertSeverity.HIGH,
                location = "Riverside District",
                time = "10 min ago",
                status = AlertStatus.ACTIVE
            ),
            Alert(
                id = "a2",
                disasterType = "Heavy Rainfall",
                severity = AlertSeverity.MEDIUM,
                location = "Downtown Area",
                time = "1 hr ago",
                status = AlertStatus.MONITORING
            ),
            Alert(
                id = "a3",
                disasterType = "Heat Wave Advisory",
                severity = AlertSeverity.LOW,
                location = "City Wide",
                time = "3 hr ago",
                status = AlertStatus.MONITORING
            )
        )
    }
}

/** Simple weather snapshot used only by the Home dashboard weather card. */
data class WeatherInfo(
    val temperatureC: Int,
    val condition: String,
    val humidity: Int,
    val windKmh: Int
)