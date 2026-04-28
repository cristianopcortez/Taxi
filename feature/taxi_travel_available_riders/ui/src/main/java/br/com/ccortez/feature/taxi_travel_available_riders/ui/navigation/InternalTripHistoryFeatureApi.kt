package br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import br.com.ccortez.core.FeatureApi
import br.com.ccortez.core.common.utils.TripHistoryFeature
import br.com.ccortez.feature.taxi_travel_available_riders.ui.screen.TripHistoryScreen
import br.com.ccortez.feature.taxi_travel_available_riders.ui.screen.TripHistoryViewModel

internal object InternalTripHistoryFeatureApi : FeatureApi {

    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(startDestination = TripHistoryFeature.taxiTravelOptionsScreenRoute,
            route = TripHistoryFeature.nestedRoute) {
            composable(
                TripHistoryFeature.taxiTravelOptionsScreenRoute,
                deepLinks = listOf(navDeepLink { uriPattern = TripHistoryFeature.deepLinkRoute })
            ) {
                val travelOptionsViewModel = hiltViewModel<TripHistoryViewModel>()
                TripHistoryScreen(travelOptionsViewModel, navController)

            }
        }
    }
}