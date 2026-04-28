package br.com.ccortez.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RideResponse(
    val id: Int,
    val date: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: DriverResponse,
    val value: Double
)