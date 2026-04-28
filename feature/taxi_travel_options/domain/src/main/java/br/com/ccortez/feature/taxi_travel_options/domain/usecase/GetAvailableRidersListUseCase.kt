package br.com.ccortez.feature.taxi_travel_options.domain.usecase

import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.repository.TaxiTravelOptionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

open class GetAvailableRidersAndRouteResponseUseCase @Inject constructor(
    private val taxiTravelOptionsRepository: TaxiTravelOptionsRepository
) {

    operator fun invoke(userId: String, originAddress: String, destinyAddress: String) = flow {
        emit(UiEvents.Loading())
        val response = taxiTravelOptionsRepository.getAvailableRidersAndRouteResponse(
            userId, originAddress, destinyAddress
        )
        emit(UiEvents.Success(
            Combined(
                availableRiders = response.availableRiders,
                origin = response.origin,
                destination = response.destination,
                distance = response.distance,
                duration = response.duration,
                options = response.options,
                routeResponse = response.routeResponse
            )
        ))
    }.catch {
        emit(UiEvents.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}

open class GetAvailableRidersListUseCase @Inject constructor(private val repository: TaxiTravelOptionsRepository) {

    operator fun invoke(userId: String, originAddress: String, destinyAddress: String) = flow {
        emit(UiEvents.Loading())
        emit(UiEvents.Success(repository.getAvailableRiders(userId, originAddress, destinyAddress)))
    }.catch {
        emit(UiEvents.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}

open class GetRouteResponseUseCase @Inject constructor(private val repository: TaxiTravelOptionsRepository) {

    operator fun invoke(userId: String, originAddress: String, destinyAddress: String) = flow {
        emit(UiEvents.Loading())
        emit(UiEvents.Success(repository.getRouteOptions(userId, originAddress, destinyAddress)))
    }.catch {
        emit(UiEvents.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}

open class GetRideConfirmUseCase @Inject constructor(private val repository: TaxiTravelOptionsRepository) {

    operator fun invoke(customerId: String, originAddress: String, destinyAddress: String,
                        distance: Int, duration: String, driverId: Int, driverName: String,
                        rideValue: Double) = flow {
        emit(UiEvents.Loading())
        val isDistanceValid = validateDistance(distance, driverId)
        if (isDistanceValid) {
            emit(UiEvents.Success(repository.rideConfirm(customerId,
                originAddress, destinyAddress,
                distance, duration, driverId, driverName,
                rideValue)))
        }
    }.catch {
        emit(UiEvents.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private fun validateDistance(distance: Int, driverId: Int): Boolean {
        // Access the minimum accepted distance from resources (assuming it's stored somewhere)
        val minimumDistance = getMinimumAcceptedDistanceFromDriver(driverId)
        return distance / 1000 >= minimumDistance
    }

    private fun getMinimumAcceptedDistanceFromDriver(driverId: Int): Int {
        return driverMinimumKmMap.get(driverId)?.minimumKm ?: 0
    }

}

data class DriverMinimumKm(val id: Int, val minimumKm: Int)

val driverMinimumKmMap = mapOf(
    1 to DriverMinimumKm(1, 1),
    2 to DriverMinimumKm(2, 5),
    3 to DriverMinimumKm(3, 10)
)