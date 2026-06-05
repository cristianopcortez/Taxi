package br.com.ccortez.feature.taxi_travel_options.ui.screen

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RouteMapScreen(_driverId: String, viewModel: RequestRideViewModel, _navController: NavController) {
    val context = LocalContext.current

    val combinedResult = viewModel.combinedResponse.value

    val mapView = remember {
        MapView(context).apply { }
    }

    AndroidView(factory = { mapView })

    combinedResult.data?.let { combined ->
        val routeResponse = combined.routeResponse

        AndroidView(factory = { _ ->
            mapView.apply {
                getMapAsync { googleMap ->
                    googleMap.apply {
                        uiSettings.isZoomControlsEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true

                        val leg = routeResponse.routes[0].legs[0]
                        val startLatLng = LatLng(
                            leg.startLocation.latLng.latitude,
                            leg.startLocation.latLng.longitude
                        )
                        val endLatLng = LatLng(
                            leg.endLocation.latLng.latitude,
                            leg.endLocation.latLng.longitude
                        )
                        googleMap.addMarker(MarkerOptions().position(startLatLng).title("Start"))
                        googleMap.addMarker(MarkerOptions().position(endLatLng).title("End"))

                        for (step in leg.steps) {
                            val polylineOptions = PolylineOptions()
                                .addAll(decodePolyline(step.polyline.encodedPolyline))
                                .color(Color.Blue.toArgb())
                                .width(10f)
                            googleMap.addPolyline(polylineOptions)
                        }

                        val bounds = LatLngBounds.builder()
                            .include(startLatLng)
                            .include(endLatLng)
                            .build()
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                        googleMap.moveCamera(cameraUpdate)
                    }
                }
            }
        })
    }
}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    var lat = 0
    var lng = 0

    while (index < encoded.length) {
        var shift = 0
        var result = 0
        var byte: Int
        do {
            byte = encoded[index++].code - 63
            result = result or (byte and 0x1f shl shift)
            shift += 5
        } while (byte >= 0x20)

        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            byte = encoded[index++].code - 63
            result = result or (byte and 0x1f shl shift)
            shift += 5
        } while (byte >= 0x20)

        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val latLng = LatLng(lat * 1e-5, lng * 1e-5)
        poly.add(latLng)
    }

    return poly
}