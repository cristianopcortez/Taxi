package br.com.ccortez.feature.taxi_travel_options.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.ccortez.core.common.ui.LoadingIndicatorWithText
import br.com.ccortez.core.common.utils.ColorBackground
import br.com.ccortez.core.common.utils.ColorTextItems
import br.com.ccortez.core.common.utils.getErrorList
import coil.compose.AsyncImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Composable
fun RouteMapScreen(
    userId: String,
    originAddress: String,
    destinyAddress: String,
    viewModel: RequestRideViewModel
) {
    val context = LocalContext.current

    val compositionComplete = remember { androidx.compose.runtime.mutableStateOf(false) }
    if (!compositionComplete.value) {
        viewModel.setQuery(userId, originAddress, destinyAddress)
        compositionComplete.value = true
    }

    val combinedResult = viewModel.combinedResponse.value

    Box(
        modifier = androidx.compose.ui.Modifier
            .background(color = ColorBackground)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            combinedResult.isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LoadingIndicatorWithText()
                }
            }

            combinedResult.error.isNotBlank() -> {
                Column {
                    AsyncImage(
                        modifier = androidx.compose.ui.Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 20.dp, end = 20.dp)
                            .fillMaxWidth()
                            .height(130.dp),
                        alignment = Alignment.Center,
                        model = getErrorList(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                        text = "Oops! There was a problem\nPlease come back again later.",
                        color = ColorTextItems,
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium
                    )
                }
            }

            combinedResult.data != null -> {
                val routeResponse = combinedResult.data.routeResponse
                val leg = routeResponse.routes[0].legs[0]

                val startLatLng = LatLng(
                    leg.startLocation.latLng.latitude,
                    leg.startLocation.latLng.longitude
                )
                val endLatLng = LatLng(
                    leg.endLocation.latLng.latitude,
                    leg.endLocation.latLng.longitude
                )

                val mapView = remember { MapView(context) }

                AndroidView(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    factory = { mapView },
                    update = { mv ->
                        mv.getMapAsync { googleMap ->
                            googleMap.uiSettings.isZoomControlsEnabled = true
                            googleMap.uiSettings.isMyLocationButtonEnabled = true

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
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                        }
                    }
                )
            }
        }
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

        poly.add(LatLng(lat * 1e-5, lng * 1e-5))
    }

    return poly
}
