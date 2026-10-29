package com.example.resqai.repository

import com.example.resqai.model.Shelter
import com.example.resqai.network.ApiClient
import com.example.resqai.network.safeApi
import com.example.resqai.network.toModel

/** Shelters from the backend (GET /shelters/), sorted nearest-first. */
class ShelterRepository {

    suspend fun getShelters(): List<Shelter> =
        safeApi { ApiClient.service.getShelters(false).items.map { it.toModel() } }
            .getOrDefault(emptyList())
            .sortedBy { it.distanceKm }

    suspend fun searchShelters(query: String): List<Shelter> {
        val all = getShelters()
        if (query.isBlank()) return all
        return all.filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }
    }
}
