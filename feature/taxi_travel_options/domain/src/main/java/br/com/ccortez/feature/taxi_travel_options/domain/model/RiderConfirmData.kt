package br.com.ccortez.feature.taxi_travel_options.domain.model

data class RiderConfirmData(
    val success: Boolean,
    val errorCode: String?,
    val errorDescription: String?,
)