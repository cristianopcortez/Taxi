package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.ccortez.feature.taxi_travel_options.ui.screen.CombinedStateHolder
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RequestRideViewModel
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RideConfirmStateHolder
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RouteMapScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class RouteMapScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockViewModel: RequestRideViewModel

    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        `when`(mockViewModel.rideConfirmResponse).thenReturn(mutableStateOf(RideConfirmStateHolder()))
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun routeMapScreen_showsLoadingIndicator_whenStateIsLoading() {
        `when`(mockViewModel.combinedResponse).thenReturn(
            mutableStateOf(CombinedStateHolder(isLoading = true))
        )

        composeTestRule.setContent {
            RouteMapScreen(
                userId = "1",
                originAddress = "Origem Teste",
                destinyAddress = "Destino Teste",
                viewModel = mockViewModel
            )
        }

        composeTestRule
            .onNodeWithText("Loading...")
            .assertIsDisplayed()
    }

    @Test
    fun routeMapScreen_showsErrorMessage_whenStateHasError() {
        `when`(mockViewModel.combinedResponse).thenReturn(
            mutableStateOf(CombinedStateHolder(error = "Falha na conexão"))
        )

        composeTestRule.setContent {
            RouteMapScreen(
                userId = "1",
                originAddress = "Origem Teste",
                destinyAddress = "Destino Teste",
                viewModel = mockViewModel
            )
        }

        composeTestRule
            .onNodeWithText("Oops! There was a problem", substring = true)
            .assertIsDisplayed()
    }
}
