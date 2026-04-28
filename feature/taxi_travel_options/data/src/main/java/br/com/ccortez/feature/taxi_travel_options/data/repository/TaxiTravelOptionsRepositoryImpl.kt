package br.com.ccortez.feature.taxi_travel_options.data.repository

import br.com.ccortez.core.network.dataproviders.TaxiTravelOptionsDataProviders
import br.com.ccortez.core.network.model.DriverDto
import br.com.ccortez.core.network.model.RideConfirmRequestDto
import br.com.ccortez.core.network.model.RideRequestDto
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainCombined
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainRiderConfirmData
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainRiderOptionList
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainRouteResponse
import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderConfirmData
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderRoutes
import br.com.ccortez.feature.taxi_travel_options.domain.repository.TaxiTravelOptionsRepository
import javax.inject.Inject

class TaxiTravelOptionsRepositoryImpl @Inject constructor(
    private val dataProviders: TaxiTravelOptionsDataProviders
) : TaxiTravelOptionsRepository {

    override suspend fun getAvailableRiders(
        userId: String, originAddress: String, destinyAddress: String
    ): List<RiderOption> {
        val riderRequestDto = RideRequestDto(customerId = userId, origin = originAddress, destination = destinyAddress)
        return dataProviders.getAvailableRiders(riderRequestDto).toDomainRiderOptionList()
    }

    override suspend fun getRouteOptions(
        userId: String, originAddress: String, destinyAddress: String
    ): RiderRoutes {
        val riderRequestDto = RideRequestDto(customerId = userId, origin = originAddress, destination = destinyAddress)
        return dataProviders.getAvailableRiders(riderRequestDto).toDomainRouteResponse()
    }

    override suspend fun getAvailableRidersAndRouteResponse(
        userId: String,
        originAddress: String,
        destinyAddress: String
    ): Combined {
        val riderRequestDto = RideRequestDto(customerId = userId, origin = originAddress, destination = destinyAddress)
        val response = dataProviders.getAvailableRidersAndRouteResponse(
            riderRequestDto
        )
        val combined = response.toDomainCombined()
        return combined
    }

    override suspend fun rideConfirm(
        customerId: String,
        originAddress: String,
        destinyAddress: String,
        distance: Int,
        duration: String,
        driverId: Int,
        driverName: String,
        rideValue: Double
    ): RiderConfirmData {
        val driverDto = DriverDto(
            id = driverId,
            name = driverName
        )
        val rideConfirmRequestDto =
            RideConfirmRequestDto(customerId = customerId, origin = originAddress,
                destination = destinyAddress, distance = distance, duration = duration,
                driver = driverDto,
                value = rideValue
            )
        return dataProviders.rideConfirm(rideConfirmRequestDto).toDomainRiderConfirmData()
    }

}