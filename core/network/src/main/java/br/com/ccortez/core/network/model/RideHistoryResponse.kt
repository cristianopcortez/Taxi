package br.com.ccortez.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RideHistoryResponse(
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<Option>,
    val routeResponse: RouteResponse // Replace Any with the actual type when known
)