package br.com.ccortez.taxi.di

import br.com.ccortez.core.common.utils.BuildConfigFieldsProvider
import br.com.ccortez.taxi.navigation.NavigationProvider
import br.com.ccortez.taxi.provider.ApplicationBuildConfigFieldsProvider
import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.AvailableRidersApi
import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.TripHistoryApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.TravelRequestFeatureApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.RiderOptionsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideNavigationProvider(travelRequestFeatureApi: TravelRequestFeatureApi, availableRidersApi: AvailableRidersApi,
                                  riderOptionsApi: RiderOptionsApi,
                                  tripHistoryApi: TripHistoryApi): NavigationProvider {
        return NavigationProvider(travelRequestFeatureApi, availableRidersApi, riderOptionsApi, tripHistoryApi)
    }

    @Provides
    internal fun provideBuildConfigFieldsProvider(
        buildConfigFieldsProvider: ApplicationBuildConfigFieldsProvider
    ): BuildConfigFieldsProvider = buildConfigFieldsProvider

}

