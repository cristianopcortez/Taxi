package br.com.ccortez.feature.taxi_travel_available_riders.domain.usecase

import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.feature.taxi_travel_available_riders.domain.repository.TaxiTravelAvailableRidersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

open class GetTripHistoryListUseCase @Inject constructor(private val repository: TaxiTravelAvailableRidersRepository) {

    operator fun invoke(userId: String, driverId: String) = flow {
        println("Executing GetTripHistoryListUseCase")
        emit(UiEvents.Loading())
        emit(UiEvents.Success(repository.getTripHistory(userId, driverId)))
    }.catch {
        println("Error in GetTripHistoryListUseCase: ${it.message}")
        emit(UiEvents.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}