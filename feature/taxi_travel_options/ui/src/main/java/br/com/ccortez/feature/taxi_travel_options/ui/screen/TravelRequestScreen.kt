package br.com.ccortez.feature.taxi_travel_options.ui.screen

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ccortez.core.common.ui.noRippleClickable
import br.com.ccortez.core.common.ui.setTagAndId
import br.com.ccortez.core.common.utils.*
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TravelRequestScreen(
    userId: String,
    originAddress: String,
    destinyAddress: String,
    viewModel: TravelOptionsViewModel,
    navController: NavController
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Use collectAsState() to observe the StateFlows
    val combinedResult by viewModel.combinedResponse.collectAsState()
    val userIdQuery by viewModel.userId.collectAsState()
    val originQuery by viewModel.originAddress.collectAsState()
    val destinyQuery by viewModel.destinyAddress.collectAsState()


    Scaffold(
        modifier = Modifier
            .background(color = ColorBackground)
            .padding(top = 8.dp)
            .noRippleClickable { keyboardController?.hide() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ColorBackground)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = "Travel Request",
                    color = ColorTextTitle,
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                        .setTagAndId("travelRequestTitle"),
                    fontWeight = FontWeight.Bold,
                    style = typography.headlineMedium
                )
                AddressInputField("Id do usuário", userIdQuery) {
                    viewModel.setQueryUserId(it)
                }
                AddressInputField("Endereço de origem", originQuery) {
                    viewModel.setQueryOriginAddress(it)
                }
                AddressInputField("Endereço de destino", destinyQuery) {
                    viewModel.setQueryDestinyAddress(it)
                }
                Button(
                    onClick = {
                        navController.navigate(
                            "rider_options/${Uri.encode(viewModel.userId.value)}/${Uri.encode(viewModel.originAddress.value)}/${Uri.encode(viewModel.destinyAddress.value)}"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                        .setTagAndId("requestTravelButton")
                ) {
                    Text(text = "Click Me")
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    combinedResult.isLoading -> LoadingIndicator()
                    combinedResult.error.isNotBlank() -> ErrorScreen(combinedResult.error)
                    combinedResult.data != null -> {
                        combinedResult.data?.let { combinedData ->
                            if (combinedData.availableRiders.isEmpty()) {
                                EmptyListScreen()
                            } else {
                                TravelOptionsContent(combinedData, navController, userIdQuery, originQuery, destinyQuery)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddressInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(
        text = label,
        color = ColorTextTitle,
        modifier = Modifier.padding(top = 4.dp),
        style = typography.bodyMedium
    )
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        TextField(
            modifier = Modifier
                .weight(4f, false)
                .setTagAndId(label),
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = ColorTextFieldText,
                focusedContainerColor = ColorTextFieldContainerDefault,
                unfocusedContainerColor = ColorTextFieldContainerDefault,
                disabledContainerColor = ColorTextFieldContainerDefault,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            placeholder = { Text(text = label) }
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .background(color = ColorBackground)
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Center the indicator
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(@Suppress("UNUSED_PARAMETER") errorMessage: String) {
    Box(
        modifier = Modifier
            .background(color = ColorBackground)
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Center content
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

@Composable
fun EmptyListScreen() {
    Box(
        modifier = Modifier
            .background(color = ColorBackground)
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Center content
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally){
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
                text = "Nenhuma viagem encontrada",
                color = ColorTextItems,
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
        }
    }
}

@Composable
fun TravelOptionsContent(
    combinedData: br.com.ccortez.feature.taxi_travel_options.domain.model.Combined,
    navController: NavController,
    userIdQuery: String,
    originQuery: String,
    destinyQuery: String
) {
    var isMapLoaded by remember { mutableStateOf(false) }

    val leg = combinedData.routeResponse
        .routes?.firstOrNull()
        ?.legs?.firstOrNull()

    Column(modifier = Modifier.fillMaxSize()) {
        if (leg != null) {
            val startLatLng = LatLng(
                leg.startLocation.latLng.latitude,
                leg.startLocation.latLng.longitude
            )
            val endLatLng = LatLng(
                leg.endLocation.latLng.latitude,
                leg.endLocation.latLng.longitude
            )
            val polylinePoints = listOf(startLatLng, endLatLng)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                StepGoogleMapView(
                    onMapLoaded = {
                        Log.d("StepGoogleMapView", "Map initialized successfully")
                        isMapLoaded = true
                    },
                    originPosition = startLatLng,
                    destinyPosition = endLatLng,
                    polylinePoints = polylinePoints
                )
                Column {
                    AnimatedVisibility(
                        visible = !isMapLoaded,
                        enter = EnterTransition.None,
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .wrapContentSize()
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(combinedData.availableRiders) { index, riderOptionItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                "rider_options/${Uri.encode(userIdQuery)}/${Uri.encode(originQuery)}/${Uri.encode(destinyQuery)}"
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorLazyGridItem[index % ColorLazyGridItem.size])
                            .padding(12.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = riderOptionItem.nome.titleCase(),
                            maxLines = 1,
                            color = ColorTextItems,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            style = typography.bodyLarge
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = riderOptionItem.descricao.titleCase(),
                            maxLines = 1,
                            color = ColorTextItems,
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = riderOptionItem.veiculo.titleCase(),
                            maxLines = 1,
                            color = ColorTextItems,
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = riderOptionItem.availacao.toString(),
                            maxLines = 1,
                            color = ColorTextItems,
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = riderOptionItem.valorDaViagem.toString(),
                            maxLines = 1,
                            color = ColorTextItems,
                            textAlign = TextAlign.Center,
                            style = typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}