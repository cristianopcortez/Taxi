package br.com.ccortez.feature.taxi_travel_available_riders.ui.di

import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.AvailableRidersApi
import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.AvailableRidersApiImpl
import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.TripHistoryApi
import br.com.ccortez.feature.taxi_travel_available_riders.ui.navigation.TripHistoryApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {

    @Provides
    fun provideAvailableRidersApi(): AvailableRidersApi {
        return AvailableRidersApiImpl()
    }

    @Provides
    fun provideTripHistoryApi(): TripHistoryApi {
        return TripHistoryApiImpl()
    }

}