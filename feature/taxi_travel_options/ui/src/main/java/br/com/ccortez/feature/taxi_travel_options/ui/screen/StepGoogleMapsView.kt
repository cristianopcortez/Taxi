package br.com.ccortez.feature.taxi_travel_options.ui.screen

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindowComposable
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun StepGoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    mapColorScheme: ComposeMapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
    content: @Composable () -> Unit = {},
    originPosition: LatLng,
    destinyPosition: LatLng,
    polylinePoints: List<LatLng>
) {
    val singaporeState = rememberMarkerState(position = singapore)
    val singapore2State = rememberMarkerState(position = singapore2)
    val singapore3State = rememberMarkerState(position = singapore3)
    val singapore4State = rememberMarkerState(position = singapore4)
    val singapore5State = rememberMarkerState(position = singapore5)
    val originLatLng = rememberMarkerState(position = originPosition)
    val destinyLatLng = rememberMarkerState(position = destinyPosition)

    var circleCenter2 by remember { mutableStateOf(singapore) }
    if (!singaporeState.isDragging) {
        circleCenter2 = singaporeState.position
    }

    val polylinePoints2 = remember { polylinePoints }
    val polylineSpanPoints2 = remember { polylinePoints }
    val styleSpanList2 = remember { listOf(styleSpan) }

    val polygonPoints2 = remember { listOf(originPosition, destinyPosition) }

    var uiSettings2 by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var ticker2 by remember { mutableIntStateOf(0) }
    var mapProperties2 by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var mapVisible2 by remember { mutableStateOf(true) }

    var darkMode2 by remember { mutableStateOf(mapColorScheme) }

    if (mapVisible2) {
        Log.d(ContentValues.TAG, "mapVisible2: ${mapVisible2}")
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties2,
            uiSettings = uiSettings2,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(ContentValues.TAG, "POI clicked: ${it.name}")
            },
            mapColorScheme = darkMode2
        ) {
            // Drawing on the map is accomplished with a child-based API
            val markerClick: (Marker) -> Boolean = {
                Log.d(ContentValues.TAG, "${it.title} was clicked")
                cameraPositionState.projection?.let { projection ->
                    Log.d(ContentValues.TAG, "The current projection is: $projection")
                }
                false
            }
            MarkerInfoWindowContent(
                state = singaporeState,
                title = "Zoom in has been tapped $ticker2 times.",
                onClick = markerClick,
                draggable = true,
            ) {
                Text(it.title ?: "Title", color = Color.Red)
            }
            MarkerInfoWindowContent(
                state = singapore2State,
                title = "Marker with custom info window.\nZoom in has been tapped $ticker2 times.",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                onClick = markerClick,
            ) {
                Text(it.title ?: "Title", color = Color.Blue)
            }
            Marker(
                state = originLatLng,
                title = "Test Marker Origin",
                onClick = markerClick
            )
            Marker(
                state = destinyLatLng,
                title = "Test Marker Destiny",
                onClick = markerClick
            )
            Marker(
                state = singapore3State,
                title = "Marker in Singapore",
                onClick = markerClick
            )
            MarkerComposable(
                title = "Marker Composable",
                keys = arrayOf("singapore4"),
                state = singapore4State,
                onClick = markerClick,
            ) {
                Box(
                    modifier = Modifier
                        .width(88.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Red),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Compose Marker",
                        textAlign = TextAlign.Center,
                    )
                }
            }
            MarkerInfoWindowComposable(
                keys = arrayOf("singapore5"),
                state = singapore5State,
                onClick = markerClick,
                title = "Marker with custom Composable info window",
                infoContent = {
                    Text(it.title ?: "Title", color = Color.Blue)
                }
            ) {
                Box(
                    modifier = Modifier
                        .width(88.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Red),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Compose MarkerInfoWindow",
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Circle(
                center = circleCenter2,
                fillColor = MaterialTheme.colorScheme.secondary,
                strokeColor = MaterialTheme.colorScheme.secondaryContainer,
                radius = 1000.0,
            )

            Polyline(
                points = polylinePoints2,
                tag = "Polyline A",
            )

            Polyline(
                points = polylineSpanPoints2,
                spans = styleSpanList2,
                tag = "Polyline B",
            )

            Polygon(
                points = polygonPoints2,
                fillColor = Color.Black.copy(alpha = 0.5f)
            )

            // Center the map on the route
            val bounds = LatLngBounds.builder()
                .include(originPosition)
                .include(destinyPosition)
                .build()
            val cameraUpdate =
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            cameraPositionState.move(cameraUpdate)

            content()
        }
    }
}