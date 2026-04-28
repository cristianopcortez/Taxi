package br.com.ccortez.feature.taxi_travel_options.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import br.com.ccortez.core.FeatureApi

interface TravelRequestFeatureApi : FeatureApi

class TravelRequestFeatureApiImpl : TravelRequestFeatureApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalTravelRequestFeatureApi.registerGraph(
            navController, navGraphBuilder
        )
    }
}
