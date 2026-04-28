package br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.ccortez.core.FeatureApi

interface AvailableRidersApi : FeatureApi

class AvailableRidersApiImpl : AvailableRidersApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalAvailableRidersFeatureApi.registerGraph(
            navController, navGraphBuilder
        )
    }
}