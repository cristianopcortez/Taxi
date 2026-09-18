package br.com.ccortez.feature.taxi_travel_options.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.ccortez.core.FeatureApi

interface RouteMapApi : FeatureApi

class RouteMapApiImpl : RouteMapApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalRouteMapFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}
