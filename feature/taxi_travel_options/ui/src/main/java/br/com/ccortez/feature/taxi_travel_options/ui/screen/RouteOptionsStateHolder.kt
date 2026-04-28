package br.com.ccortez.feature.taxi_travel_options.ui.screen

import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderRoutes

data class RouteOptionsStateHolder(
    val isLoading: Boolean = false,
    val data: RiderRoutes? = null,
    val error: String = ""
)
