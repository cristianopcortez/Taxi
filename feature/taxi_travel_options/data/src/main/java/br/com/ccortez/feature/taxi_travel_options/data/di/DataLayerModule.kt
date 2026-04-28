package br.com.ccortez.feature.taxi_travel_options.data.di

import br.com.ccortez.core.network.dataproviders.TaxiTravelOptionsDataProviders
import br.com.ccortez.feature.taxi_travel_options.data.repository.TaxiTravelOptionsRepositoryImpl
import br.com.ccortez.feature.taxi_travel_options.domain.repository.TaxiTravelOptionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataLayerModule {

    @Provides
    fun provideTaxiTravelOptionsRepository(taxiTravelOptionsDataProviders: TaxiTravelOptionsDataProviders): TaxiTravelOptionsRepository {
        return TaxiTravelOptionsRepositoryImpl(taxiTravelOptionsDataProviders)
    }

}