package br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import br.com.ccortez.core.FeatureApi
import br.com.ccortez.core.common.utils.AvailableDriversFeature
import br.com.ccortez.feature.taxi_travel_available_riders.ui.screen.AvailableRidersScreen
import br.com.ccortez.feature.taxi_travel_available_riders.ui.screen.TripHistoryViewModel

internal object InternalAvailableRidersFeatureApi : FeatureApi {

    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(startDestination = AvailableDriversFeature.taxiTravelOptionsScreenRoute,
            route = AvailableDriversFeature.nestedRoute) {
            composable(
                AvailableDriversFeature.taxiTravelOptionsScreenRoute,
                deepLinks = listOf(navDeepLink { uriPattern = AvailableDriversFeature.deepLinkRoute })
            ) {
                val userId = it.arguments?.getString("userId")
                val originAddress = it.arguments?.getString("originAddress")
                val destinyAddress = it.arguments?.getString("destinyAddress")
                val travelOptionsViewModel = hiltViewModel<TripHistoryViewModel>()
                AvailableRidersScreen(userId.toString(), originAddress.toString(),
                    destinyAddress.toString(), travelOptionsViewModel, navController)

            }
        }
    }
}