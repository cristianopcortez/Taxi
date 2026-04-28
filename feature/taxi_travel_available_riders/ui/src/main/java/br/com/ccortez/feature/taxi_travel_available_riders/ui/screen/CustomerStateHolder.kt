package br.com.ccortez.feature.taxi_travel_available_riders.ui.screen

import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Ride

data class CustomerStateHolder(
    val isLoading: Boolean = false,
    val data: List<Ride>? = null,
    val error: String = ""
)
