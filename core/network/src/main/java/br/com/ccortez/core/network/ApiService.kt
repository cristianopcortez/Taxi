package br.com.ccortez.core.network

import br.com.ccortez.core.network.model.AvailableRidersResponse
import br.com.ccortez.core.network.model.CombinedResponse
import br.com.ccortez.core.network.model.CustomerResponse
import br.com.ccortez.core.network.model.RideConfirmRequestDto
import br.com.ccortez.core.network.model.RideConfirmResponse
import br.com.ccortez.core.network.model.RideRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @PATCH("ride/confirm")
    suspend fun rideConfirm(
        @Body data: RideConfirmRequestDto
    ): RideConfirmResponse

    @POST("ride/estimate")
    suspend fun getAvailableRiders(
        @Body data: RideRequestDto
    ): AvailableRidersResponse

    @POST("ride/estimate")
    suspend fun getAvailableRidersAndRouteResponse(
        @Body data: RideRequestDto
    ): CombinedResponse

    @GET("ride/{customer_id}")
    suspend fun getTripHistory(
        @Path("customer_id") customerId: String,
        @Query("driver_id") driverId: String?
    ): CustomerResponse

}