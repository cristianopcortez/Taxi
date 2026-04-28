package br.com.ccortez.core.common.utils

object TaxiTravelRequestFeature {
    const val nestedRoute = "taxi_travel_request_nested_route"
    const val taxiTravelRequestcreenRoute = "taxi_travel_request_screen_route"
    const val deepLinkRoute = "taxi://"
}

object AvailableDriversFeature {
    const val nestedRoute = "available_drivers_nested_route"
    const val taxiTravelOptionsScreenRoute = "available_drivers/{userId}/{originAddress}/{destinyAddress}"
    const val deepLinkRoute = "taxi://available_drivers/{userId}/{originAddress}/{destinyAddress}"
}

object TripHistoryFeature {
    const val nestedRoute = "trip_history_nested_route"
    const val taxiTravelOptionsScreenRoute = "trip_history"
    const val deepLinkRoute = "taxi://trip_history/{userId}/{driverId}"
}

object RouteMapFeature {
    const val nestedRoute = "route_map_nested_route"
    const val taxiTravelOptionsScreenRoute = "route_map/{id}"
    const val deepLinkRoute = "taxi://route_map/{id}"
}

object RiderOptionsFeature {
    const val nestedRoute = "rider_options_nested_route"
    const val taxiTravelOptionsScreenRoute = "rider_options/{userId}/{originAddress}/{destinyAddress}"
    const val deepLinkRoute = "taxi://rider_options/{userId}/{originAddress}/{destinyAddress}"
}