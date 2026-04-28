package br.com.ccortez.feature.taxi_travel_options.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ccortez.core.common.ui.ConfirmDialog
import br.com.ccortez.core.common.ui.LoadingIndicatorWithText
import br.com.ccortez.core.common.ui.noRippleClickable
import br.com.ccortez.core.common.ui.setTagAndId
import com.google.android.gms.maps.model.LatLng
import coil.compose.AsyncImage
import br.com.ccortez.core.common.utils.ColorBackground
import br.com.ccortez.core.common.utils.ColorLazyGridItem
import br.com.ccortez.core.common.utils.ColorTextFieldContainerDefault
import br.com.ccortez.core.common.utils.ColorTextFieldText
import br.com.ccortez.core.common.utils.ColorTextItems
import br.com.ccortez.core.common.utils.ColorTextTitle
import br.com.ccortez.core.common.utils.getEmptyList
import br.com.ccortez.core.common.utils.getErrorList
import br.com.ccortez.core.common.utils.titleCase

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RiderOptionsScreen(userId: String, originAddress: String, destinyAddress: String,
                        viewModel: RequestRideViewModel, navController: NavController) {

    val rideConfirmResponse = viewModel.rideConfirmResponse.value
    val combinedResult = viewModel.combinedResponse.value

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val compositionComplete = remember { mutableStateOf(false) }

    if (!compositionComplete.value) {
        viewModel.setQuery(userId, originAddress, destinyAddress)
        compositionComplete.value = true
    }

    Scaffold(
        modifier = Modifier
            .background(color = ColorBackground)
            .padding(top = 8.dp)
            .noRippleClickable {
                keyboardController?.hide()
            },
        topBar = {
            Column(
                modifier = Modifier
                    .background(color = ColorBackground)
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = "Available Riders",
                    color = ColorTextTitle,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .setTagAndId("availableRidersTitle"),
                    fontWeight = FontWeight.Bold,
                    style = typography.headlineMedium
                )
            }
        }) { screen ->
        Log.e("TAG", "RiderOptionsScreen: $screen")

        if (rideConfirmResponse.isLoading) {
            Toast.makeText(context, "Processing ride confirm....", Toast.LENGTH_SHORT).show()
        }
        if (rideConfirmResponse.error.isNotBlank()) {
            Toast.makeText(context, "Error in confirming ride.... please try again in some minutes", Toast.LENGTH_SHORT).show()
        }
        rideConfirmResponse.data?.let {
            ConfirmDialog(
                dialogTitle = "Confirm Ride",
                dialogText = "Are you sure to confirm this ride ?",
                onConfirmAction = { navController.navigate("trip_history") }
            )
        }

        if (combinedResult.isLoading) {
            Box(
                modifier = Modifier
                    .background(color = ColorBackground).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally in the Column
                ) {
                    LoadingIndicatorWithText()
                }
            }
        }
        if (combinedResult.error.isNotBlank()) {
            Box(
                modifier = Modifier
                    .background(color = ColorBackground).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    AsyncImage(
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                start = 20.dp,
                                end = 20.dp
                            )
                            .fillMaxWidth()
                            .height(130.dp),
                        alignment = Alignment.Center,
                        model = getErrorList(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Oops! There was a problem\nPlease come back again later.",
                        color = ColorTextItems,
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium
                    )
                }
            }
        }
        combinedResult.data?.let {
            if (combinedResult.data.availableRiders.isEmpty()) {
                Box(
                    modifier = Modifier.background(color = ColorBackground).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        AsyncImage(
                            modifier = Modifier
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                )
                                .fillMaxWidth()
                                .height(130.dp),
                            alignment = Alignment.Center,
                            model = getEmptyList(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Nenhuma opção de viagem encontrada",
                            color = ColorTextItems,
                            textAlign = TextAlign.Center,
                            style = typography.titleMedium
                        )
                    }
                }
            } else {
                var isMapLoaded by remember { mutableStateOf(false) }

                combinedResult.data.routeResponse.let { routeResponse ->

                    Box(
                        modifier = Modifier
                            .background(color = ColorBackground).fillMaxSize()
                            .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                            .height(130.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {


                        Column(
                            modifier = Modifier
                                .height(230.dp)
                                .width(230.dp),
                        ) {

                            // Add markers for start and end locations
                            val startLatLng = LatLng(
                                routeResponse.routes[0].legs[0].startLocation.latLng.latitude,
                                routeResponse.routes[0].legs[0].startLocation.latLng.longitude
                            )
                            val endLatLng = LatLng(
                                routeResponse.routes[0].legs[0].endLocation.latLng.latitude,
                                routeResponse.routes[0].legs[0].endLocation.latLng.longitude
                            )


                            // Define a list of points for the polyline
                            val polylinePoints = listOf(
                                startLatLng, // Origin
                                endLatLng, // Destiny
                            )

                            Log.d("StepGoogleMapView", "Marker added at startLatLng: ${startLatLng}")

                            StepGoogleMapView(
                                onMapLoaded = {
                                    Log.d("StepGoogleMapView", "Map initialized successfully")
                                    isMapLoaded = true
                                },
                                originPosition = startLatLng,
                                destinyPosition = endLatLng,
                                polylinePoints = polylinePoints
                            )
                            if (!isMapLoaded) {
                                AnimatedVisibility(
                                    modifier = Modifier
                                        .height(130.dp),
                                    visible = !isMapLoaded,
                                    enter = EnterTransition.None,
                                    exit = fadeOut()
                                ) {
                                    LoadingIndicatorWithText()
                                }
                            }
                        }
                    }

                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .padding(top = 350.dp, start = 16.dp, end = 16.dp),
                    contentPadding = PaddingValues(16.dp), // Padding around the grid
                    verticalArrangement = Arrangement.spacedBy(8.dp), // Spacing between rows
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between columns
                ) {
                    itemsIndexed(
                        combinedResult.data.availableRiders) { index, riderOptionItem ->
                        Box(
                            modifier = Modifier
                                .background(color = Color.Cyan)
                                .aspectRatio(1f) // Ensures a square shape
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(150.dp)
                                    .padding(8.dp)
                                    .clickable {
                                    },
                                shape = RoundedCornerShape(16.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .background(ColorLazyGridItem[index % ColorLazyGridItem.size])
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 12.dp,
                                                end = 12.dp
                                            ),
                                        text = riderOptionItem.nome.titleCase(),
                                        maxLines = 1,
                                        color = ColorTextItems,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        style = typography.bodyLarge
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 12.dp,
                                                end = 12.dp
                                            ),
                                        text = riderOptionItem.descricao.titleCase(),
                                        maxLines = 1,
                                        color = ColorTextItems,
                                        textAlign = TextAlign.Center,
                                        style = typography.bodyMedium
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 12.dp,
                                                end = 12.dp
                                            ),
                                        text = riderOptionItem.veiculo.titleCase(),
                                        maxLines = 1,
                                        color = ColorTextItems,
                                        textAlign = TextAlign.Center,
                                        style = typography.bodyMedium
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 12.dp,
                                                end = 12.dp
                                            ),
                                        text = riderOptionItem.availacao.toString(),
                                        maxLines = 1,
                                        color = ColorTextItems,
                                        textAlign = TextAlign.Center,
                                        style = typography.bodyMedium
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 12.dp,
                                                end = 12.dp
                                            ),
                                        text = riderOptionItem.valorDaViagem.toString(),
                                        maxLines = 1,
                                        color = ColorTextItems,
                                        textAlign = TextAlign.Center,
                                        style = typography.bodyMedium
                                    )
                                    Button(
                                        onClick = {
                                            combinedResult.data.let { data ->
                                                viewModel.rideConfirm(
                                                    customerId = userId,
                                                    originAddress = originAddress,
                                                    destinyAddress = destinyAddress,
                                                    distance = data.distance,
                                                    duration = data.duration.toString(),
                                                    driverId = riderOptionItem.id,
                                                    driverName = riderOptionItem.nome,
                                                    rideValue = riderOptionItem.valorDaViagem
                                                )
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = "Confirm Ride")
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}
