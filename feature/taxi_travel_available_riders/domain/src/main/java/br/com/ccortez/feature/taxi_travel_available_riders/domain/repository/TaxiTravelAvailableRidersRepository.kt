package br.com.ccortez.feature.taxi_travel_available_riders.domain.repository

import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Customer

interface TaxiTravelAvailableRidersRepository {



    suspend fun getTripHistory(userId: String, driverId: String): Customer

}