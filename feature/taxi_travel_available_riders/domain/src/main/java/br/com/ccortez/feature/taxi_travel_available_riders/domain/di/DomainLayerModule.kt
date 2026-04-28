package br.com.ccortez.feature.taxi_travel_available_riders.domain.di

import br.com.ccortez.feature.taxi_travel_available_riders.domain.repository.TaxiTravelAvailableRidersRepository
import br.com.ccortez.feature.taxi_travel_available_riders.domain.usecase.GetTripHistoryListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DomainLayerModule {

    @Provides
    fun provideGetTaxiTravelOptionsListUseCase(repository: TaxiTravelAvailableRidersRepository): GetTripHistoryListUseCase {
        return GetTripHistoryListUseCase(repository)
    }

}