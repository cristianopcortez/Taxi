package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersListUseCase
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelOptionsViewModel
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelRequestScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class TravelRequestScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var requestRideViewModel: TravelOptionsViewModel

    @Mock
    lateinit var getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase

    @Mock
    lateinit var getAvailableRidersListUseCase: GetAvailableRidersListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun myTempTest() {

        requestRideViewModel = TravelOptionsViewModel(
            getAvailableRidersAndRouteResponseUseCase,
            getAvailableRidersListUseCase,
        )

        // Arrange the mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        // Start the app
        composeTestRule.setContent {
            TravelRequestScreen(
                "", "", "",
                requestRideViewModel, mockNavController
            )
        }

        // Perform actions and assertions
        composeTestRule.onNodeWithText("Travel Request").assertIsDisplayed()
    }
}