package br.com.ccortez.feature.taxi_travel_options.domain.model

data class RiderRoutes(
    val origin: LatLng,
    val destination: LatLng,
    val distance: Int,
    val duration: Int,
    val options: List<RiderOption>,
    val routeResponse: RouteResponse
)

data class RouteResponse(
    val routes: List<Route>,
    val geocodingResults: GeocodingResults
)

data class Combined(
    val availableRiders: List<RiderOption>,
    val origin: LatLng,
    val destination: LatLng,
    val distance: Int,
    val duration: Int,
    val options: List<RiderOption>,
    val routeResponse: RouteResponse
)

data class Route(
   val distanceMeters: Int,
    val duration: String,
    val staticDuration: String,
    val polyline: Polyline,
    val legs: List<Leg>
)

data class Location(
    val latLng: LatLng
)

data class Leg(
    val distanceMeters: Int,
    val duration: String,
    val staticDuration: String,
    val polyline: Polyline,
    val startLocation: Location,
    val endLocation: Location,
    val steps: List<Step>,
    val localizedValues: LocalizedValues
)

data class Polyline(
    val encodedPolyline: String
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class Step(
    val distanceMeters: Int,
    val staticDuration: String,
    val polyline: Polyline,
    val startLocation: Location,
    val endLocation: Location,
    val navigationInstruction: NavigationInstruction,
    val localizedValues: LocalizedValues,
    val travelMode: String
)

data class NavigationInstruction(
    val maneuver: String,
    val instructions: String
)

data class LocalizedValues(
    val distance: Distance,
    val staticDuration: Duration
)

data class Distance(
    val text: String
)

data class Duration(
    val text: String
)

data class GeocodingResults(
    val origin: GeocodingResult,
    val destination: GeocodingResult
)

data class GeocodingResult(
    val geocoderStatus: GeocoderStatus,
    val type: List<String>,
    val placeId: String
)

data class GeocoderStatus(
    val code: Int? = null, // Optional, if the status code is provided
    val message: String? = null // Optional, if a status message is provided
)