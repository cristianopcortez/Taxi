package br.com.ccortez.feature.taxi_travel_options.domain.repository

import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderConfirmData
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderRoutes

interface TaxiTravelOptionsRepository {

    suspend fun getAvailableRiders(userId: String, originAddress: String, destinyAddress: String): List<RiderOption>

    suspend fun getRouteOptions(
        userId: String, originAddress: String, destinyAddress: String
    ): RiderRoutes

    suspend fun getAvailableRidersAndRouteResponse(
        userId: String,
        originAddress: String,
        destinyAddress: String
    ): Combined

    suspend fun rideConfirm(
        customerId: String, originAddress: String, destinyAddress: String,
        distance: Int, duration: String, driverId: Int, driverName: String,
        rideValue: Double
    ): RiderConfirmData

}