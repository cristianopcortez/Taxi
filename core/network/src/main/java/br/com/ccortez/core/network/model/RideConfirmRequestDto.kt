package br.com.ccortez.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RideConfirmRequestDto(
    // usando gson até´ver problema de serialização com kotlin serializable
    @SerializedName("customer_id")var customerId: String,

    val origin: String,
    val destination: String,

    val distance: Int? = null,

    val duration: String? = null,

    val driver: DriverDto? = null,

    val value: Double? = null
)

/*import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RideConfirmRequestDto(
    @SerialName("customer_id") var customerId: String,

    @SerialName("origin") val origin: String,
    @SerialName("destination") val destination: String,

    @SerialName("distance") val distance: Int? = null,

    @SerialName("duration") val duration: String? = null,

    @SerialName("driver") val driver: DriverDto? = null,

    @SerialName("value") val value: Double? = null
)*/

@Serializable
data class DriverDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)