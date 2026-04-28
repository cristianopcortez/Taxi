package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.core.network.model.RideRequestDto
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainCombined
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersListUseCase
import br.com.ccortez.feature.taxi_travel_options.ui.api.FakeApiService
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelOptionsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension  // Keep MockitoExtension if you need it for other things

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class) // Keep this if you have other Mockito mocks
class GetAvailableRidersAndRouteResponseUseCaseMockkJUnit5Test {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Use MockK for mocking
    private val getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase =
        mockk()
    private val getAvailableRidersListUseCase: GetAvailableRidersListUseCase = mockk()


    private lateinit var viewModel: TravelOptionsViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = TravelOptionsViewModel(
            getAvailableRidersAndRouteResponseUseCase,
            getAvailableRidersListUseCase
        )
    }

    @Test
    fun `test getCombinedData updates combinedResponse state`() = runTest {
        // Arrange
        val userId = "1"
        val originAddress = "Origin"
        val destinyAddress = "Destiny"
        val rideRequestDto = RideRequestDto(userId, originAddress, destinyAddress)

        val expectedCombinedData =
            FakeApiService().getAvailableRidersAndRouteResponse(rideRequestDto).toDomainCombined()

        // Use coEvery for suspending functions in MockK
        coEvery {
            getAvailableRidersAndRouteResponseUseCase(
                userId,
                originAddress,
                destinyAddress
            )
        } returns flowOf(UiEvents.Success(expectedCombinedData))

        coEvery {
            getAvailableRidersListUseCase(userId, originAddress, destinyAddress)
        } returns flowOf(UiEvents.Loading())


        // Act
        viewModel.setQueryUserId(userId)
        viewModel.setQueryOriginAddress(originAddress)
        viewModel.setQueryDestinyAddress(destinyAddress)

        // Await the *first* emission from the combined flow.  This is key!
        val combinedResponseValue = viewModel.combinedResponse.first()

        // Assert
        assertNotNull(combinedResponseValue)
        assertFalse(combinedResponseValue.isLoading)
        assertEquals(expectedCombinedData, combinedResponseValue.data)
        assertEquals("", combinedResponseValue.error)
    }


    @Test
    fun `test getCombinedData handles error`() = runTest {
        // Arrange
        val userId = "1"
        val originAddress = "Origin"
        val destinyAddress = "Destiny"
        val errorMessage = "Network error"

        // Mock the use case to return an error event, using coEvery
        coEvery {
            getAvailableRidersAndRouteResponseUseCase(userId, originAddress, destinyAddress)
        } returns flowOf(UiEvents.Error(errorMessage))

        coEvery {
            getAvailableRidersListUseCase(userId, originAddress, destinyAddress)
        } returns flowOf(UiEvents.Loading())

        // Act
        viewModel.setQueryUserId(userId)
        viewModel.setQueryOriginAddress(originAddress)
        viewModel.setQueryDestinyAddress(destinyAddress)

        // Await the first emission
        val combinedResponseValue = viewModel.combinedResponse.first()

        // Assert
        assertNotNull(combinedResponseValue)
        assertFalse(combinedResponseValue.isLoading)
        assertEquals(null, combinedResponseValue.data)
        assertEquals(errorMessage, combinedResponseValue.error)
    }

    @Test
    fun `test getCombinedData loading state`() = runTest {
        //Arrange
        val userId = "1"
        val originAddress = "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001"
        val destinyAddress = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"

        // Use coEvery for suspending functions
        coEvery {
            getAvailableRidersAndRouteResponseUseCase(userId, originAddress, destinyAddress)
        } returns flowOf(UiEvents.Loading())

        coEvery {
            getAvailableRidersListUseCase(userId, originAddress, destinyAddress)
        } returns flowOf(UiEvents.Loading())

        //Act
        viewModel.setQueryUserId(userId)
        viewModel.setQueryOriginAddress(originAddress)
        viewModel.setQueryDestinyAddress(destinyAddress)

        // Await the first emission
        val combinedResponseValue = viewModel.combinedResponse.first()

        //Assert
        assertNotNull(combinedResponseValue)
        assertTrue(combinedResponseValue.isLoading)
    }
}