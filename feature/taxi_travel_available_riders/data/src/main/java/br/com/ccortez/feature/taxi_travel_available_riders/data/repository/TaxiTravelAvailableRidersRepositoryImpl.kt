package br.com.ccortez.feature.taxi_travel_available_riders.data.repository

import br.com.ccortez.core.network.dataproviders.RideHistoryDataProviders
import br.com.ccortez.feature.taxi_travel_available_riders.data.mapper.toCustomer
import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Customer
import br.com.ccortez.feature.taxi_travel_available_riders.domain.repository.TaxiTravelAvailableRidersRepository
import javax.inject.Inject

class TaxiTravelAvailableRidersRepositoryImpl @Inject constructor(
    private val dataProviders: RideHistoryDataProviders
) : TaxiTravelAvailableRidersRepository {

    override suspend fun getTripHistory(
        userId: String,
        driverId: String
    ): Customer {
        return dataProviders.getTripHistory(userId, driverId).toCustomer()
    }

}