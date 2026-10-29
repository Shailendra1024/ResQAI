package com.example.resqai.repository

import com.example.resqai.model.Alert
import com.example.resqai.network.ApiClient
import com.example.resqai.network.safeApi
import com.example.resqai.network.toModel

/** Alerts from the backend (GET /alerts/). Returns an empty list / null if the server is unreachable. */
class AlertRepository {

    suspend fun getAlerts(): List<Alert> =
        safeApi { ApiClient.service.getAlerts(null).items.map { it.toModel() } }
            .getOrDefault(emptyList())

    suspend fun getAlertById(id: String): Alert? =
        safeApi { ApiClient.service.getAlert(id).toModel() }.getOrNull()
}
