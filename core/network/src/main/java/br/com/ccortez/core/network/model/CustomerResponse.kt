package br.com.ccortez.core.network.model

/*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerResponse(
    @SerialName("customer_id") val customerId: String,
    val rides: List<RideResponse>
)
*/

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    // usando gson até´ver problema de serialização com kotlin serializable
    @SerializedName("customer_id")var customerId: String,
    val rides: List<RideResponse>
)

