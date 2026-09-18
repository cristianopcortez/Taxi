package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.ccortez.core.datastore.UserPreferencesRepository
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersListUseCase
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelOptionsViewModel
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelRequestScreen
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.AutoCloseable
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class TravelRequestScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var requestRideViewModel: TravelOptionsViewModel

    @Mock
    lateinit var getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase

    @Mock
    lateinit var getAvailableRidersListUseCase: GetAvailableRidersListUseCase

    @Mock
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)

        `when`(userPreferencesRepository.lastUserId).thenReturn(flowOf(""))
        `when`(userPreferencesRepository.lastOriginAddress).thenReturn(flowOf(""))
        `when`(userPreferencesRepository.lastDestinyAddress).thenReturn(flowOf(""))

        requestRideViewModel = TravelOptionsViewModel(
            getAvailableRidersAndRouteResponseUseCase,
            getAvailableRidersListUseCase,
            userPreferencesRepository,
        )
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun myTempTest() {
        val mockNavController = Mockito.mock(NavController::class.java)

        composeTestRule.setContent {
            TravelRequestScreen(
                "", "", "",
                requestRideViewModel, mockNavController
            )
        }

        composeTestRule.onNodeWithText("Travel Request").assertIsDisplayed()
    }
}
