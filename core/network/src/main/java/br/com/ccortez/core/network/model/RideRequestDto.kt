package br.com.ccortez.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class RideRequestDto(
    @SerializedName("customer_id") var customerId: String,
    @SerialName("origin") val origin: String,
    @SerialName("destination") var destination: String
)