package br.com.ccortez.feature.taxi_travel_available_riders.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
@Suppress("UNUSED_PARAMETER")
@Composable
fun AvailableRidersScreen(userId: String, originAddress: String, destinyAddress: String,
                          viewModel: TripHistoryViewModel, navController: NavController) {

    val result = viewModel.tripHistory.value
    val userIdQuery = viewModel.userId.collectAsState()
    val driverQuery = viewModel.driverId.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                    text = "Histórico de viagens",
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
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    TextField(
                        modifier = Modifier
                            .weight(4f),
                        value = driverQuery.value,
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
                        placeholder = { Text(text = "Id de motorista") }
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
                                  },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Click Me")
                    }
                }
            }
        }) { screen ->

        if (result.isLoading) {
            Log.i("TAG", "AvailableRidersScreen: $screen")
            Box(modifier = Modifier
                .background(color = ColorBackground).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        if (result.error.isNotBlank()) {
            Log.e("TAG", "AvailableRidersScreen: $screen")
            Box(modifier = Modifier
                .background(color = ColorBackground).fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
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
        result.data.let {
            if (it != null) {
                if (it.isEmpty()) {
                    Box(modifier = Modifier.background(color = ColorBackground).fillMaxSize(), contentAlignment = Alignment.Center) {
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
                                text = "Nenhuma viagem encontrada",
                                color = ColorTextItems,
                                textAlign = TextAlign.Center,
                                style = typography.titleMedium
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .background(color = ColorBackground)
                            .padding(top = 180.dp, start = 16.dp, end = 16.dp),
                        columns = GridCells.Fixed(2),
                        content = {
                            itemsIndexed(it) { index, it ->
                                Card(
                                    modifier = Modifier
                                        .height(212.dp)
                                        .padding(8.dp),
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
                                            text = it.driver.name.titleCase(),
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
                                            text = it.value.toString(),
                                            maxLines = 1,
                                            color = ColorTextItems,
                                            textAlign = TextAlign.Center,
                                            style = typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
