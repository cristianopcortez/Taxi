package br.com.ccortez.feature.taxi_travel_options.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import br.com.ccortez.core.FeatureApi
import br.com.ccortez.core.common.utils.TaxiTravelRequestFeature
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelRequestScreen
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelOptionsViewModel

internal object InternalTravelRequestFeatureApi : FeatureApi {

    override fun registerGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation(startDestination = TaxiTravelRequestFeature.taxiTravelRequestcreenRoute,
            route = TaxiTravelRequestFeature.nestedRoute) {
            composable(
                TaxiTravelRequestFeature.taxiTravelRequestcreenRoute,
                deepLinks = listOf(navDeepLink { uriPattern = TaxiTravelRequestFeature.deepLinkRoute })
            ) {
                val userId = it.arguments?.getString("userId")
                val originAddress = it.arguments?.getString("originAddress")
                val destinyAddress = it.arguments?.getString("destinyAddress")
                val travelOptionsViewModel = hiltViewModel<TravelOptionsViewModel>()
                TravelRequestScreen(userId.toString(), originAddress.toString(),
                    destinyAddress.toString(), travelOptionsViewModel, navController)

            }
        }
    }
}
