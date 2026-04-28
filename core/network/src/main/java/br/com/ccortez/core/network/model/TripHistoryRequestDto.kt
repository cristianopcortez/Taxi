package br.com.ccortez.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripHistoryRequestDto(
    @SerialName("customer_id") var customerId: String,
    @SerialName("driver_id") val driverId: String
)