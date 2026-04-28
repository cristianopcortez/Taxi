package br.com.ccortez.taxi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.ccortez.core.common.utils.TaxiTravelRequestFeature

@Composable
fun AppNavGraph(
    navController: NavHostController,
    navigationProvider: NavigationProvider
) {

    NavHost(navController = navController, startDestination = TaxiTravelRequestFeature.nestedRoute){
        navigationProvider.travelRequestFeatureApi.registerGraph(
            navController,this
        )

        navigationProvider.availableRidersApi.registerGraph(
            navController,this
        )

        navigationProvider.riderOptionsApi.registerGraph(
            navController,this
        )

        navigationProvider.tripHistoryApi.registerGraph(
            navController,this
        )

    }
}