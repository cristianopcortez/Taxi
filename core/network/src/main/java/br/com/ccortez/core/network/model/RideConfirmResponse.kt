package br.com.ccortez.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RideConfirmResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("error_code") val errorCode: String?,
    @SerialName("error_description") val errorDescription: String?
)