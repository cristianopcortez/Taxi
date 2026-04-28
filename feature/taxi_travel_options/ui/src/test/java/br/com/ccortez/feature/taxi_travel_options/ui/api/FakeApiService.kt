package br.com.ccortez.feature.taxi_travel_options.ui.api

import br.com.ccortez.core.network.ApiService
import br.com.ccortez.core.network.model.AvailableRidersResponse
import br.com.ccortez.core.network.model.CombinedResponse
import br.com.ccortez.core.network.model.CustomerResponse
import br.com.ccortez.core.network.model.Distance
import br.com.ccortez.core.network.model.Duration
import br.com.ccortez.core.network.model.GeocoderStatus
import br.com.ccortez.core.network.model.GeocodingResult
import br.com.ccortez.core.network.model.GeocodingResults
import br.com.ccortez.core.network.model.LatLng
import br.com.ccortez.core.network.model.Leg
import br.com.ccortez.core.network.model.LocalizedValues
import br.com.ccortez.core.network.model.Location
import br.com.ccortez.core.network.model.NavigationInstruction
import br.com.ccortez.core.network.model.Option
import br.com.ccortez.core.network.model.Polyline
import br.com.ccortez.core.network.model.Review
import br.com.ccortez.core.network.model.RideConfirmRequestDto
import br.com.ccortez.core.network.model.RideConfirmResponse
import br.com.ccortez.core.network.model.RideRequestDto
import br.com.ccortez.core.network.model.Route
import br.com.ccortez.core.network.model.RouteResponse
import br.com.ccortez.core.network.model.Step
import br.com.ccortez.feature.taxi_travel_options.ui.JsonProvider
import java.lang.Exception
import javax.inject.Inject

class FakeApiService @Inject constructor() : ApiService {

    var failUserApi: Boolean = false
    var wrongResponse: Boolean = false

    override suspend fun rideConfirm(data: RideConfirmRequestDto): RideConfirmResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAvailableRiders(data: RideRequestDto): AvailableRidersResponse {
        if (failUserApi) throw Exception("Api failed")
        val fakeResponse: CombinedResponse = JsonProvider.objectFromJsonFileWithType(
            COMBINED_LIST_JSON
        )

        val availableRidersResponse = AvailableRidersResponse(
            origin = fakeResponse.origin,
            destination = fakeResponse.destination,
            distance = fakeResponse.distance,
            duration = fakeResponse.duration,
            options = fakeResponse.options,
            routeResponse = fakeResponse.routeResponse
        )

        if (wrongResponse) return availableRidersResponse

        return availableRidersResponse
    }

    override suspend fun getAvailableRidersAndRouteResponse(data: RideRequestDto): CombinedResponse {
        if (failUserApi) throw Exception("Api failed")
        val fakeResponse: CombinedResponse = JsonProvider.objectFromJsonFileWithType(
            COMBINED_LIST_JSON
        )

        if (wrongResponse) return fakeResponse.apply {
            CombinedResponse(
            origin = LatLng(0.0,0.0),
            destination = LatLng(0.0,0.0),
            distance = 0,
            duration = 0,
            options = listOf(
                Option(
                id = 0,
                name =  "",
                description = "",
                vehicle = "",
                review = Review(0, ""),
                value = 0.0
                )
            ),
            routeResponse = routeResponse
            )
        }

        return fakeResponse
    }

    override suspend fun getTripHistory(customerId: String, driverId: String?): CustomerResponse {
        TODO("Not yet implemented")
    }

    companion object {
        private const val COMBINED_LIST_JSON = "/json/ride-estimate.json"
        val routeResponse = RouteResponse(
            routes = listOf(
                Route(
                    distanceMeters = 2990,
                    duration = "634s",
                    staticDuration = "634s",
                    polyline = Polyline(encodedPolyline = "dwynCtj{{GdCwDjCwC^_@PSHKV[^_@bAeALKVYlBqBz@aAr@y@`@a@pB{B|@_ALODCXY@O@M?QIa@RcDJuAc@Ce@EOCIAEAKCICGCGEECIEGECCQOqBsBqBwBo@m@CEIKq@m@mAeAIGa@e@GGGEa@a@oD_Ew@w@i@k@s@s@s@u@qAuAQQCAMOg@g@oCmCKM]_@MMKKCCEGSSUUY[OS]]KKq@u@OOY[YUc@g@AAOMQSCCc@g@CCGGYYi@i@_@a@k@i@Y_@DGb@k@T[V]dAsAb@e@FGjAmA@CjBwBo@q@CCo@q@k@o@ECSUGGKKm@t@CDIHUXCD"),
                    legs = listOf(
                        Leg(
                            distanceMeters = 2990,
                            duration = "634s",
                            staticDuration = "634s",
                            polyline = Polyline(encodedPolyline = "dwynCtj{{GdCwDjCwC^_@PSHKV[^_@bAeALKVYlBqBz@aAr@y@`@a@pB{B|@_ALODCXY@O@M?QIa@RcDJuAc@Ce@EOCIAEAKCICGCGEECIEGECCQOqBsBqBwBo@m@CEIKq@m@mAeAIGa@e@GGGEa@a@oD_Ew@w@i@k@s@s@s@u@qAuAQQCAMOg@g@oCmCKM]_@MMKKCCEGSSUUY[OS]]KKq@u@OOY[YUc@g@AAOMQSCCc@g@CCGGYYi@i@_@a@k@i@Y_@DGb@k@T[V]dAsAb@e@FGjAmA@CjBwBo@q@CCo@q@k@o@ECSUGGKKm@t@CDIHUXCD"),
                            startLocation = Location(latLng = LatLng(latitude = -23.566114499999998, longitude = -46.675793399999996)),
                            endLocation = Location(latLng = LatLng(latitude = -23.5615351, longitude = -46.6562816)),
                            steps = listOf(
                                Step(
                                    distanceMeters = 771,
                                    staticDuration = "128s",
                                    polyline = Polyline(encodedPolyline = "dwynCtj{{GdCwDjCwC^_@PSHKV[^_@bAeALKVYlBqBz@aAr@y@`@a@pB{B|@_ALODCXY"),
                                    startLocation = Location(latLng = LatLng(latitude = -23.566114499999998, longitude = -46.675793399999996)),
                                    endLocation = Location(latLng = LatLng(latitude = -23.570975999999998, longitude = -46.6704163)),
                                    navigationInstruction = NavigationInstruction(maneuver = "DEPART", instructions = "Siga para sudeste na Av. Brasil em direção a R. Guadelupe\nEncerrada Domingos entre as 07:00-16:00"),
                                    localizedValues = LocalizedValues(
                                        distance = Distance(text = "0,8 km"),
                                        staticDuration = Duration(text = "2 minutos")
                                    ),
                                    travelMode = "DRIVE"
                                ),
                                Step(
                                    distanceMeters = 171,
                                    staticDuration = "40s",
                                    polyline = Polyline(encodedPolyline = "ruznCbiz{G@O@M?QIa@RcDJuA"),
                                    startLocation = Location(latLng = LatLng(latitude = -23.570975999999998, longitude = -46.6704163)),
                                    endLocation = Location(latLng = LatLng(latitude = -23.5711123, longitude = -46.6687589)),
                                    navigationInstruction = NavigationInstruction(maneuver = "TURN_LEFT", instructions = "Vire à esquerda em direção a R. Bolívia"),
                                    localizedValues = LocalizedValues(
                                        distance = Distance(text = "0,2 km"),
                                        staticDuration = Duration(text = "1 min")
                                    ),
                                    travelMode = "DRIVE"
                                ),
                                //... other steps
                            ),
                            localizedValues = LocalizedValues(
                                distance = Distance(text = "3,0 km"),
                                staticDuration = Duration(text = "11 minutos")
                            )
                        )
                    )
                )
            ),
            geocodingResults = GeocodingResults(
                origin = GeocodingResult(
                    geocoderStatus = GeocoderStatus(),
                    type = listOf("street_address"),
                    placeId = "EkVBdi4gQnJhc2lsLCAyMDMzIC0gSmFyZGltIEFtZXJpY2EsIFPDo28gUGF1bG8gLSBTUCwgMDE0MzEtMDAxLCBCcmF6aWwiMRIvChQKEgmRwqOyflfOlBEbfxGjwses1xDxDyoUChIJ0U9qcNhZzpQRbMqz2uTNjFM"
                ),
                destination = GeocodingResult(
                    geocoderStatus = GeocoderStatus(),
                    type = listOf("street_address"),
                    placeId = "ChIJvS4WJslZzpQRfufYwqjow_Y"
                )
            )
        )

    }
}