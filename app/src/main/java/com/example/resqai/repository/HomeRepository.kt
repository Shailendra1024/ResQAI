package com.example.resqai.repository

import com.example.resqai.model.Alert
import com.example.resqai.model.EmergencyContact
import com.example.resqai.model.Shelter
import kotlinx.coroutines.delay

/**
 * Home dashboard. Alerts and shelters now come from the backend.
 * Risk status, weather and emergency contacts are still local placeholders (see TODOs).
 */
class HomeRepository {
    private val alertRepository = AlertRepository()
    private val shelterRepository = ShelterRepository()

    // TODO: call POST /risk/estimate with real weather data (needs a weather source first).
    suspend fun getRiskStatus(): String {
        delay(300)
        return "Moderate Risk"
    }

    // TODO: replace with a real weather API (e.g. Open-Meteo).
    suspend fun getWeather(): WeatherInfo {
        delay(300)
        return WeatherInfo(temperatureC = 29, condition = "Partly Cloudy", humidity = 68, windKmh = 14)
    }

    suspend fun getNearbyShelters(): List<Shelter> =
        shelterRepository.getShelters().take(3)

    // National emergency numbers are static on purpose: they must work offline.
    suspend fun getEmergencyContacts(): List<EmergencyContact> {
        return listOf(
            EmergencyContact(id = "c1", name = "Police", phoneNumber = "100"),
            EmergencyContact(id = "c2", name = "Fire Department", phoneNumber = "101"),
            EmergencyContact(id = "c3", name = "Ambulance", phoneNumber = "102")
        )
    }

    suspend fun getLatestAlerts(): List<Alert> =
        alertRepository.getAlerts().take(3)
}

/** Simple weather snapshot used only by the Home dashboard weather card. */
data class WeatherInfo(
    val temperatureC: Int,
    val condition: String,
    val humidity: Int,
    val windKmh: Int
)
