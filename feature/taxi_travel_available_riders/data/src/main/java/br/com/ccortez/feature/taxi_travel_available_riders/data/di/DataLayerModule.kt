package br.com.ccortez.feature.taxi_travel_available_riders.data.di

import br.com.ccortez.core.network.dataproviders.RideHistoryDataProviders
import br.com.ccortez.feature.taxi_travel_available_riders.data.repository.TaxiTravelAvailableRidersRepositoryImpl
import br.com.ccortez.feature.taxi_travel_available_riders.domain.repository.TaxiTravelAvailableRidersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {

    @Provides
    fun provideTaxiTravelOptionsRepository(rideHistoryDataProviders: RideHistoryDataProviders): TaxiTravelAvailableRidersRepository {
        return TaxiTravelAvailableRidersRepositoryImpl(rideHistoryDataProviders)
    }

}