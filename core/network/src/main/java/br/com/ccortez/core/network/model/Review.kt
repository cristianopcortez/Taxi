package br.com.ccortez.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val rating: Int,
    val comment: String
)