package br.com.ccortez.feature.taxi_travel_options.ui.screen

import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption

data class RiderOptionStateHolder(
    val isLoading: Boolean = false,
    val data: List<RiderOption>? = null,
    val error: String = ""
)

data class CombinedStateHolder(
    val isLoading: Boolean = false,
    val data: Combined? = null,
    val error: String = ""
)
