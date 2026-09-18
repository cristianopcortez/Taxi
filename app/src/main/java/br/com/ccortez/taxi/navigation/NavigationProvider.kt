package br.com.ccortez.taxi.navigation

import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.AvailableRidersApi
import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.TripHistoryApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.TravelRequestFeatureApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.RiderOptionsApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.RouteMapApi

data class NavigationProvider(
    val travelRequestFeatureApi: TravelRequestFeatureApi,
    val availableRidersApi: AvailableRidersApi,
    val riderOptionsApi: RiderOptionsApi,
    val tripHistoryApi: TripHistoryApi,
    val routeMapApi: RouteMapApi
)
