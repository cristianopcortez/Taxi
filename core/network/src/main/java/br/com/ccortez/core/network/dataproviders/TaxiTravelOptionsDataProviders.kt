package br.com.ccortez.core.network.dataproviders

import br.com.ccortez.core.network.ApiService
import br.com.ccortez.core.network.model.CombinedResponse
import br.com.ccortez.core.network.model.RideConfirmRequestDto
import br.com.ccortez.core.network.model.RideRequestDto
import javax.inject.Inject

class TaxiTravelOptionsDataProviders @Inject constructor(private val apiService: ApiService) {

    suspend fun getAvailableRiders(rideRequest: RideRequestDto) = apiService.getAvailableRiders(rideRequest)

    suspend fun getAvailableRidersAndRouteResponse(rideRequest: RideRequestDto): CombinedResponse {
        return apiService.getAvailableRidersAndRouteResponse(rideRequest)
    }

    suspend fun rideConfirm(rideConfirmDto: RideConfirmRequestDto) = apiService.rideConfirm(rideConfirmDto)

}