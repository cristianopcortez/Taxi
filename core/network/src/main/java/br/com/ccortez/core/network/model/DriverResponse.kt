package br.com.ccortez.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverResponse(
    val id: Int,
    val name: String
)