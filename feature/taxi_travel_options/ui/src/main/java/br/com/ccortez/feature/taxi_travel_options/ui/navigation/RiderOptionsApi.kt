package br.com.ccortez.feature.taxi_travel_options.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.ccortez.core.FeatureApi

interface RiderOptionsApi : FeatureApi

class RiderOptionsApiImpl : RiderOptionsApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalRiderOptionsFeatureApi.registerGraph(
            navController, navGraphBuilder
        )
    }
}