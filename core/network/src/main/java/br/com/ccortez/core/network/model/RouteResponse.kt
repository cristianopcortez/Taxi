package br.com.ccortez.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    @SerialName("routes") val routes: List<Route>,
    @SerialName("geocodingResults") val geocodingResults: GeocodingResults
)

@Serializable
data class Route(
    @SerialName("distanceMeters") val distanceMeters: Int,
    @SerialName("duration") val duration: String,
    @SerialName("staticDuration") val staticDuration: String,
    @SerialName("polyline") val polyline: Polyline,
    @SerialName("legs") val legs: List<Leg>
)

@Serializable
data class Leg(
    @SerialName("distanceMeters") val distanceMeters: Int,
    @SerialName("duration") val duration: String,
    @SerialName("staticDuration") val staticDuration: String,
    @SerialName("polyline") val polyline: Polyline,
    @SerialName("startLocation") val startLocation: Location,
    @SerialName("endLocation") val endLocation: Location,
    @SerialName("steps") val steps: List<Step>,
    @SerialName("localizedValues") val localizedValues: LocalizedValues
)

@Serializable
data class Polyline(
    @SerialName("encodedPolyline") val encodedPolyline: String
)

@Serializable
data class LatLng(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double
)

@Serializable
data class Step(
    @SerialName("distanceMeters") val distanceMeters: Int,
    @SerialName("staticDuration") val staticDuration: String,
    @SerialName("polyline") val polyline: Polyline,
    @SerialName("startLocation") val startLocation: Location,
    @SerialName("endLocation") val endLocation: Location,
    @SerialName("navigationInstruction") val navigationInstruction: NavigationInstruction,
    @SerialName("localizedValues") val localizedValues: LocalizedValues,
    @SerialName("travelMode") val travelMode: String
)

@Serializable
data class NavigationInstruction(
    @SerialName("maneuver") val maneuver: String,
    @SerialName("instructions") val instructions: String
)

@Serializable
data class LocalizedValues(
    @SerialName("distance") val distance: Distance,
    @SerialName("staticDuration") val staticDuration: Duration
)

@Serializable
data class Distance(
    @SerialName("text") val text: String
)

@Serializable
data class Duration(
    @SerialName("text") val text: String
)

@Serializable
data class GeocodingResults(
    @SerialName("origin") val origin: GeocodingResult,
    @SerialName("destination") val destination: GeocodingResult
)

@Serializable
data class GeocodingResult(
    @SerialName("geocoderStatus") val geocoderStatus: GeocoderStatus,
    @SerialName("type") val type: List<String>,
    @SerialName("placeId") val placeId: String
)

@Serializable
data class GeocoderStatus(
    @SerialName("code") val code: Int? = null, // Optional, if the status code is provided
    @SerialName("message") val message: String? = null // Optional, if a status message is provided
)