package br.com.ccortez.feature.taxi_travel_options.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import br.com.ccortez.core.FeatureApi
import br.com.ccortez.core.common.utils.AvailableDriversFeature
import br.com.ccortez.core.common.utils.RouteMapFeature
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RequestRideViewModel
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RouteMapScreen

internal object InternalRouteMapFeatureApi : FeatureApi {

    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(startDestination = RouteMapFeature.taxiTravelOptionsScreenRoute,
            route = AvailableDriversFeature.nestedRoute) {
            composable(
                RouteMapFeature.taxiTravelOptionsScreenRoute,
                deepLinks = listOf(navDeepLink { uriPattern = RouteMapFeature.deepLinkRoute })
            ) {
                val viewModel = hiltViewModel<RequestRideViewModel>()
                val driverId = it.arguments?.getString("driverId")
                RouteMapScreen(driverId.toString(), viewModel, navController)
            }
        }
    }
}