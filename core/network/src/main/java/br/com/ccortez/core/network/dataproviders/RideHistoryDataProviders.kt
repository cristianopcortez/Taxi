package br.com.ccortez.core.network.dataproviders

import br.com.ccortez.core.network.ApiService
import javax.inject.Inject

class RideHistoryDataProviders @Inject constructor(private val apiService: ApiService) {

    suspend fun getTripHistory(userId: String, driverId: String) = apiService.getTripHistory(userId, driverId)

}