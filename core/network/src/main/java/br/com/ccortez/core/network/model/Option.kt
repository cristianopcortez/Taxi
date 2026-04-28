package br.com.ccortez.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Option(
    val id: Int,
    val name: String,
    val description: String,
    val vehicle: String,
    val review: Review,
    val value: Double
)