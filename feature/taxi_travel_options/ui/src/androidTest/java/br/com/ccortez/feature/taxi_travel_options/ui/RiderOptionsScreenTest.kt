package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.model.Distance
import br.com.ccortez.feature.taxi_travel_options.domain.model.Duration
import br.com.ccortez.feature.taxi_travel_options.domain.model.GeocoderStatus
import br.com.ccortez.feature.taxi_travel_options.domain.model.GeocodingResult
import br.com.ccortez.feature.taxi_travel_options.domain.model.GeocodingResults
import br.com.ccortez.feature.taxi_travel_options.domain.model.LatLng
import br.com.ccortez.feature.taxi_travel_options.domain.model.Leg
import br.com.ccortez.feature.taxi_travel_options.domain.model.LocalizedValues
import br.com.ccortez.feature.taxi_travel_options.domain.model.Location
import br.com.ccortez.feature.taxi_travel_options.domain.model.Polyline
import br.com.ccortez.feature.taxi_travel_options.domain.model.RiderOption
import br.com.ccortez.feature.taxi_travel_options.domain.model.Route
import br.com.ccortez.feature.taxi_travel_options.domain.model.RouteResponse
import br.com.ccortez.feature.taxi_travel_options.ui.screen.CombinedStateHolder
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RequestRideViewModel
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RideConfirmStateHolder
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RiderOptionsScreen
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.AutoCloseable
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class RiderOptionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockViewModel: RequestRideViewModel

    private lateinit var mockNavController: NavController

    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        mockNavController = Mockito.mock(NavController::class.java)
        `when`(mockViewModel.rideConfirmResponse).thenReturn(mutableStateOf(RideConfirmStateHolder()))
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun riderOptionsScreen_showsTitle_whenStateIsLoading() {
        `when`(mockViewModel.combinedResponse).thenReturn(
            mutableStateOf(CombinedStateHolder(isLoading = true))
        )

        composeTestRule.setContent {
            RiderOptionsScreen(
                userId = "1",
                originAddress = "Origem Teste",
                destinyAddress = "Destino Teste",
                viewModel = mockViewModel,
                navController = mockNavController
            )
        }

        composeTestRule
            .onNodeWithTag("availableRidersTitle", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun riderOptionsScreen_showsErrorMessage_whenStateHasError() {
        `when`(mockViewModel.combinedResponse).thenReturn(
            mutableStateOf(CombinedStateHolder(error = "Falha na conexão"))
        )

        composeTestRule.setContent {
            RiderOptionsScreen(
                userId = "1",
                originAddress = "Origem Teste",
                destinyAddress = "Destino Teste",
                viewModel = mockViewModel,
                navController = mockNavController
            )
        }

        composeTestRule
            .onNodeWithText("Oops! There was a problem", substring = true)
            .assertIsDisplayed()
    }

    /**
     * Verifica que o botão "Ver Rota Completa" aparece quando há opções de motorista disponíveis.
     * 
     * TODO: Re-enable this test using runTest + Dispatchers.Main configuration when setting up
     *       proper Google Play Services integration for instrumented tests.
     *       Reference: https://developer.android.com/kotlin/coroutines/test#setting-main-dispatcher
     */
    @Ignore("Requires Google Play Services and main thread dispatcher configuration for MapView rendering")
    @Test
    fun riderOptionsScreen_showsVerRotaCompletaButton_whenDataHasRiders() {
        `when`(mockViewModel.combinedResponse).thenReturn(
            mutableStateOf(CombinedStateHolder(data = fakeCombined()))
        )

        composeTestRule.setContent {
            RiderOptionsScreen(
                userId = "1",
                originAddress = "Origem Teste",
                destinyAddress = "Destino Teste",
                viewModel = mockViewModel,
                navController = mockNavController
            )
        }

        composeTestRule
            .onNodeWithTag("verRotaCompletaButton", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    private fun fakeCombined(): Combined {
        val fakeLatLng = LatLng(latitude = -23.5505, longitude = -46.6333)
        val fakeLocation = Location(fakeLatLng)
        val fakeLeg = Leg(
            distanceMeters = 5000,
            duration = "10 min",
            staticDuration = "10 min",
            polyline = Polyline(encodedPolyline = ""),
            startLocation = fakeLocation,
            endLocation = fakeLocation,
            steps = emptyList(),
            localizedValues = LocalizedValues(
                distance = Distance(text = "5 km"),
                staticDuration = Duration(text = "10 min")
            )
        )
        val fakeRoute = Route(
            distanceMeters = 5000,
            duration = "10 min",
            staticDuration = "10 min",
            polyline = Polyline(encodedPolyline = ""),
            legs = listOf(fakeLeg)
        )
        val fakeRouteResponse = RouteResponse(
            routes = listOf(fakeRoute),
            geocodingResults = GeocodingResults(
                origin = GeocodingResult(
                    geocoderStatus = GeocoderStatus(),
                    type = emptyList(),
                    placeId = ""
                ),
                destination = GeocodingResult(
                    geocoderStatus = GeocoderStatus(),
                    type = emptyList(),
                    placeId = ""
                )
            )
        )
        return Combined(
            availableRiders = listOf(
                RiderOption(
                    id = 1,
                    nome = "Motorista Teste",
                    descricao = "Econômico",
                    veiculo = "Sedan",
                    availacao = 5,
                    valorDaViagem = 25.50
                )
            ),
            origin = fakeLatLng,
            destination = fakeLatLng,
            distance = 5000,
            duration = 600,
            options = emptyList(),
            routeResponse = fakeRouteResponse
        )
    }
}
