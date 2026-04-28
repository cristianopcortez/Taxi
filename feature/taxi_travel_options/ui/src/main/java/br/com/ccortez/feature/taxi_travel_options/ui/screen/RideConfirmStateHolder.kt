package br.com.ccortez.feature.taxi_travel_options.ui.screen

import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderConfirmData

data class RideConfirmStateHolder(
    val isLoading: Boolean = false,
    val data: RiderConfirmData? = null,
    val error: String = ""
)
