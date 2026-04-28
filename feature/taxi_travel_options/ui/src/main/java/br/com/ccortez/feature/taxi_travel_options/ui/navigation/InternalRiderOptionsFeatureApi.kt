package br.com.ccortez.feature.taxi_travel_options.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import br.com.ccortez.core.FeatureApi
import br.com.ccortez.core.common.utils.RiderOptionsFeature
import br.com.ccortez.core.common.utils.RouteMapFeature
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RequestRideViewModel
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RiderOptionsScreen

internal object InternalRiderOptionsFeatureApi : FeatureApi {

    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(startDestination = RiderOptionsFeature.taxiTravelOptionsScreenRoute,
            route = RiderOptionsFeature.nestedRoute) {
            composable(
                RiderOptionsFeature.taxiTravelOptionsScreenRoute,
                deepLinks = listOf(navDeepLink { uriPattern = RouteMapFeature.deepLinkRoute })
            ) {
                val viewModel = hiltViewModel<RequestRideViewModel>()
                val userId = it.arguments?.getString("userId")
                val originAddress = it.arguments?.getString("originAddress")
                val destinyAddress = it.arguments?.getString("destinyAddress")

                RiderOptionsScreen(userId.toString(), originAddress.toString(),
                    destinyAddress.toString(), viewModel, navController)
            }
        }
    }
}