package br.com.ccortez.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CombinedResponse(
    val origin: LatLng,
    val destination: LatLng,
    val distance: Int,
    val duration: Int,
    val options: List<Option>,
    @SerialName("routeResponse") val routeResponse: RouteResponse
)