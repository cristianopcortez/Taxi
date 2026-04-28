package br.com.ccortez.feature.taxi_travel_options.ui.di

import br.com.ccortez.feature.taxi_travel_options.ui.navigation.TravelRequestFeatureApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.TravelRequestFeatureApiImpl
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.RiderOptionsApi
import br.com.ccortez.feature.taxi_travel_options.ui.navigation.RiderOptionsApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {

    @Provides
    fun provideTravelRequestFeatureApi(): TravelRequestFeatureApi {
        return TravelRequestFeatureApiImpl()
    }

    @Provides
    fun provideAvailableRidersApi(): RiderOptionsApi {
        return RiderOptionsApiImpl()
    }

}