package br.com.ccortez.feature.taxi_travel_available_riders.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.ccortez.core.common.utils.ColorBackground
import br.com.ccortez.core.common.utils.ColorTextTitle
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import br.com.ccortez.core.common.ui.LoadingIndicatorWithText
import br.com.ccortez.core.common.ui.noRippleClickable
import br.com.ccortez.core.common.utils.ColorTextFieldContainerDefault
import br.com.ccortez.core.common.utils.ColorTextFieldText
import br.com.ccortez.core.common.utils.ColorTextItems
import br.com.ccortez.core.common.utils.getEmptyList
import br.com.ccortez.feature.taxi_travel_available_riders.domain.model.Ride
import coil.compose.AsyncImage
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TripHistoryScreen(viewModel: TripHistoryViewModel, _navController: NavController) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val userIdQuery = viewModel.userId.collectAsState()
    val driverIdQuery = viewModel.driverId.collectAsState()

    val result = viewModel.tripHistory.value

    var showOtherContent by remember { mutableStateOf(true) } // State for the other content


    Scaffold(
        modifier = Modifier
            .background(color = ColorBackground)
            .padding(top = 8.dp)
            .noRippleClickable {
                keyboardController?.hide()
            },
        topBar = {
            if (showOtherContent) {
                Column(
                    modifier = Modifier
                        .background(color = ColorBackground)
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp)
                ) {
                    Text(
                        text = "Trip History",
                        color = ColorTextTitle,
                        modifier = Modifier
                            .padding(top = 20.dp),
                        fontWeight = FontWeight.Bold,
                        style = typography.headlineMedium
                    )
                    Text(
                        text = "Id do usuário",
                        color = ColorTextTitle,
                        modifier = Modifier
                            .padding(top = 4.dp),
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
                                .weight(4f),
                            value = userIdQuery.value,
                            onValueChange = {
                                viewModel.setQueryUserId(it)
                            },
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
                            placeholder = { Text(text = "id do usuário") }
                        )
                    }
                    Text(
                        text = "Id do motorista",
                        color = ColorTextTitle,
                        modifier = Modifier
                            .padding(top = 4.dp),
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
                                .weight(4f),
                            value = driverIdQuery.value,
                            onValueChange = {
                                viewModel.setQueryDriverId(it)
                            },
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
                            placeholder = { Text(text = "id do motorista") }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.setQueryUserId("CT01")
                                viewModel.setQueryDriverId("1")
                                viewModel.setQuery("abcd") // This triggers the debounced API call
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Click Me")
                        }
                    }
                }
            }
        }) { screen ->
        Log.e("TAG", "TripHistoryScreen: $screen")

        if (result.isLoading) {
            Box(
                modifier = Modifier
                    .background(color = ColorBackground)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                LoadingIndicatorWithText()
            }
        }
        if (result.error.isNotBlank()) {
            Toast.makeText(context, "Error in get ride history.... please try again in some minutes", Toast.LENGTH_SHORT).show()
        }
        result.data?.let {
            if (it.isEmpty()) {
                Box(
                    modifier = Modifier
                        .background(color = ColorBackground)
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                    ) {
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
            } else {
                showOtherContent = false
                Column(
                    modifier = Modifier
                        .background(color = ColorBackground)
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp)
                ) {
                    Text(
                        text = "Rider History",
                        color = ColorTextTitle,
                        modifier = Modifier
                            .padding(top = 20.dp),
                        fontWeight = FontWeight.Bold,
                        style = typography.headlineMedium
                    )
                    CustomerRideView(result.data)
                }


            }
        }
    }
}

@Composable
fun CustomerRideView(rides: List<Ride>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (rides.isEmpty()) {
            Text("No rides found for this customer.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Spacing between items
                items(rides) { ride ->
                    RideItem(ride = ride)
                }
            }
        }
    }
}

@Composable
fun RideItem(ride: Ride) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Correct way to set elevation
        modifier = Modifier.fillMaxWidth()

    ) { // Use Card for visual appeal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Increased padding
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {
                Text("Ride ID: ${ride.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp) // Larger ID
                Text(formatDate(ride.date), fontSize = 14.sp, color = Color.Gray) // Formatted date
            }

            Spacer(modifier = Modifier.height(8.dp)) // Increased spacing

            // Origin and Destination with Labels
            LabeledText(label = "Origin:", text = ride.origin)
            LabeledText(label = "Destination:", text = ride.destination)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LabeledText(label = "Distance:", text = "${formatDistance(ride.distance)} km")
                LabeledText(label = "Duration:", text = ride.duration)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LabeledText(label = "Driver:", text = ride.driver.name, fontWeight = FontWeight.SemiBold) // Driver more prominent
            LabeledText(label = "Value:", text = "$${ride.value}", color = Color.Green) // Value in green


        }
    }
}

@Composable
fun LabeledText(label: String, text: String, fontWeight: FontWeight = FontWeight.Normal, color: Color = Color.Black) {
    Row {
        Text(label, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontWeight = fontWeight, color = color)
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()) // Adjust format if needed
        val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!)
    } catch (_: Exception) {
        dateString // Return original string if parsing fails
    }
}

fun formatDistance(distance: Double): String {
    val df = DecimalFormat("0.0") // Format to one decimal place
    return "${df.format(distance)}"
}
