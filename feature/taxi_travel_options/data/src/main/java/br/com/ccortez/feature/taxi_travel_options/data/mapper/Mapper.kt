package br.com.ccortez.feature.taxi_travel_options.data.mapper

import br.com.ccortez.core.network.model.AvailableRidersResponse
import br.com.ccortez.core.network.model.CombinedResponse
import br.com.ccortez.core.network.model.GeocoderStatus
import br.com.ccortez.core.network.model.GeocodingResult
import br.com.ccortez.core.network.model.GeocodingResults
import br.com.ccortez.core.network.model.Leg
import br.com.ccortez.core.network.model.LocalizedValues
import br.com.ccortez.core.network.model.Location
import br.com.ccortez.core.network.model.NavigationInstruction
import br.com.ccortez.core.network.model.RouteResponse
import br.com.ccortez.core.network.model.Route
import br.com.ccortez.core.network.model.Step
import br.com.ccortez.core.network.model.Option
import br.com.ccortez.core.network.model.RideConfirmResponse
import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.model.Polyline
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderConfirmData

fun RideConfirmResponse.toDomainRiderConfirmData(): RiderConfirmData {
    return RiderConfirmData(
        errorDescription = errorDescription,
        success = success,
        errorCode = errorCode
    )
}

fun AvailableRidersResponse.toDomainRiderOptionList(): List<br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption> {
    return this.options.map {
        br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption(
            id = it.id,
            nome = it.name,
            descricao = it.description,
            veiculo = it.vehicle,
            availacao = it.review.rating,
            valorDaViagem = it.value

        )
    }
}

fun List<Option>.toRiderOptions(): List<br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption> {
    return this.map { option ->
        option.toRiderOption()
    }
}

fun Option.toRiderOption(): br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption(
        id = id,
        nome = name,
        descricao = description,
        veiculo = vehicle,
        availacao = review.rating,
        valorDaViagem = value
    )
}

fun Leg.toDomainLeg(): br.com.ccortez.feature.taxi_travel_options.domain.model.Leg {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.Leg(
        distanceMeters = distanceMeters,
        duration = duration,
        staticDuration = staticDuration,
        polyline = Polyline(encodedPolyline = polyline.encodedPolyline),
        startLocation = startLocation.toTargetLocation(),
        endLocation = endLocation.toTargetLocation(),
        steps = steps.toTargetSteps(),
        localizedValues = localizedValues.toTargetLocalizedValues()
    )
}

fun Step.toTargetStep(): br.com.ccortez.feature.taxi_travel_options.domain.model.Step {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.Step(
        distanceMeters = distanceMeters,
        staticDuration = staticDuration,
        polyline = Polyline(encodedPolyline = polyline.encodedPolyline),
        startLocation = startLocation.toTargetLocation(),
        endLocation = endLocation.toTargetLocation(),
        navigationInstruction = navigationInstruction.toTargetNavigationInstruction(),
        localizedValues = localizedValues.toTargetLocalizedValues(),
        travelMode = travelMode
    )
}

fun List<Step>.toTargetSteps(): List<br.com.ccortez.feature.taxi_travel_options.domain.model.Step> {
    return this.map { sourceStep ->
        sourceStep.toTargetStep()
    }
}

fun List<Leg>.toTargetLegs(): List<br.com.ccortez.feature.taxi_travel_options.domain.model.Leg> {
    return this.map { sourceLeg ->
        sourceLeg.toDomainLeg()
    }
}

fun LocalizedValues.toTargetLocalizedValues(): br.com.ccortez.feature.taxi_travel_options.domain.model.LocalizedValues {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.LocalizedValues(
        distance = br.com.ccortez.feature.taxi_travel_options.domain.model.Distance(text = distance.text),
        staticDuration = br.com.ccortez.feature.taxi_travel_options.domain.model.Duration(text = staticDuration.text)
    )
}

fun NavigationInstruction.toTargetNavigationInstruction(): br.com.ccortez.feature.taxi_travel_options.domain.model.NavigationInstruction {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.NavigationInstruction(
        maneuver = maneuver,
        instructions = instructions
    )
}

fun Location.toTargetLocation(): br.com.ccortez.feature.taxi_travel_options.domain.model.Location {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.Location(
        latLng = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(this.latLng.latitude, this.latLng.longitude)
    )
}

fun RouteResponse.toTargetRouteResponse(): br.com.ccortez.feature.taxi_travel_options.domain.model.RouteResponse {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.RouteResponse(
        routes = routes.toTargetRoutes(),
        geocodingResults = geocodingResults.toTargetGeocodingResults()
    )
}

fun CombinedResponse.toDomainCombined(): Combined {
    return Combined(
        availableRiders = options.toRiderOptions(),
        origin = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(origin.latitude, origin.longitude),
        destination = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(destination.latitude, destination.longitude),
        distance = distance,
        duration = duration,
        options = options.toRiderOptions(),
        routeResponse = this.routeResponse.toTargetRouteResponse()
    )
}

fun AvailableRidersResponse.toDomainRouteResponse(): br.com.ccortez.feature.taxi_travel_options.domain.model.RiderRoutes {
    val route = routeResponse.routes.firstOrNull() ?: return br.com.ccortez.feature.taxi_travel_options.domain.model.RiderRoutes(
        origin = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(origin.latitude, origin.longitude),
        destination = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(destination.latitude, destination.longitude),
        distance = distance,
        duration = duration,
        options = options.toRiderOptions(),
        routeResponse = routeResponse.toTargetRouteResponse()
    )

    return br.com.ccortez.feature.taxi_travel_options.domain.model.RiderRoutes(
        origin = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(origin.latitude, origin.longitude),
        destination = br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng(destination.latitude, destination.longitude),
        distance = distance,
        duration = duration,
        options = this.toDomainRiderOptionList(),
        routeResponse = br.com.ccortez.feature.taxi_travel_options.domain.model.RouteResponse(
            routes = listOf(
                br.com.ccortez.feature.taxi_travel_options.domain.model.Route(
                    distanceMeters = route.distanceMeters,
                    duration = route.duration,
                    staticDuration = route.staticDuration,
                    polyline = Polyline(encodedPolyline = route.polyline.encodedPolyline),
                    legs = route.legs.map { it.toDomainLeg() }
                )
            ),
            geocodingResults = routeResponse.geocodingResults.toTargetGeocodingResults()
        )
    )
}

fun GeocodingResults.toTargetGeocodingResults(): br.com.ccortez.feature.taxi_travel_options.domain.model.GeocodingResults {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.GeocodingResults(
        origin = origin.toTargetGeocodingResult(),
        destination = destination.toTargetGeocodingResult()
    )
}

fun GeocodingResult.toTargetGeocodingResult(): br.com.ccortez.feature.taxi_travel_options.domain.model.GeocodingResult {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.GeocodingResult(
        geocoderStatus = geocoderStatus.toTargetGeocoderStatus(),
        type = type,
        placeId = placeId
    )
}

fun GeocoderStatus.toTargetGeocoderStatus(): br.com.ccortez.feature.taxi_travel_options.domain.model.GeocoderStatus {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.GeocoderStatus(
        code = code,
        message = message
    )
}

fun Route.toTargetRoute(): br.com.ccortez.feature.taxi_travel_options.domain.model.Route {
    return br.com.ccortez.feature.taxi_travel_options.domain.model.Route(
        distanceMeters = distanceMeters,
        duration = duration,
        staticDuration = staticDuration,
        polyline = Polyline(encodedPolyline = polyline.encodedPolyline),
        legs = legs.toTargetLegs()
    )
}

fun List<Route>.toTargetRoutes(): List<br.com.ccortez.feature.taxi_travel_options.domain.model.Route> {
    return this.map { sourceRoute ->
        sourceRoute.toTargetRoute()
    }
}