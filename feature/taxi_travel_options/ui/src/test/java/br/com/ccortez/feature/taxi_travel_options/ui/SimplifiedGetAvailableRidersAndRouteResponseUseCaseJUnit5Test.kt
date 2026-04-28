package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.core.network.model.RideRequestDto
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainCombined
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersListUseCase
import br.com.ccortez.feature.taxi_travel_options.ui.api.FakeApiService
import br.com.ccortez.feature.taxi_travel_options.ui.screen.TravelOptionsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class SimplifiedGetAvailableRidersAndRouteResponseUseCaseJUnit5Test {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase

    @Mock
    private lateinit var getAvailableRidersListUseCase: GetAvailableRidersListUseCase

    private lateinit var viewModel: TravelOptionsViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined) // Crucial for immediate execution
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

        `when`(
            getAvailableRidersAndRouteResponseUseCase(
                userId,
                originAddress,
                destinyAddress
            )
        ).thenReturn(flowOf(UiEvents.Success(expectedCombinedData)))

        `when`(
            getAvailableRidersListUseCase(userId, originAddress, destinyAddress)
        ).thenReturn(flowOf(UiEvents.Loading()))


        // Act: Set the query parameters.  The ViewModel's `init` block will trigger the use case.
        viewModel.setQueryUserId(userId)
        viewModel.setQueryOriginAddress(originAddress)
        viewModel.setQueryDestinyAddress(destinyAddress)

        // Advance time until all coroutines are idle
        advanceUntilIdle()

        // Await the *first* emission from the combined flow. This ensures the use case has run.
        val combinedResponseValue = viewModel.combinedResponse.value

        // Assert:  Now, check the state *after* the flow has emitted.
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

        // Mock the use case to return an error event
        `when`(
            getAvailableRidersAndRouteResponseUseCase(userId, originAddress, destinyAddress)
        ).thenReturn(flowOf(UiEvents.Error(errorMessage)))

        `when`(
            getAvailableRidersListUseCase(userId, originAddress, destinyAddress)
        ).thenReturn(flowOf(UiEvents.Loading()))


        // Act
        viewModel.setQueryUserId(userId)
        viewModel.setQueryOriginAddress(originAddress)
        viewModel.setQueryDestinyAddress(destinyAddress)

        // Await the first emission, which should contain the error
        val combinedResponseValue = viewModel.combinedResponse.value


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

        `when`(
            getAvailableRidersAndRouteResponseUseCase(userId, originAddress, destinyAddress)
        ).thenReturn(flowOf(UiEvents.Loading()))

        `when`(
            getAvailableRidersListUseCase(userId, originAddress, destinyAddress)
        ).thenReturn(flowOf(UiEvents.Loading()))


        //Act
        viewModel.setQueryUserId(userId)
        viewModel.setQueryOriginAddress(originAddress)
        viewModel.setQueryDestinyAddress(destinyAddress)

        // Await the first emission, which should be the loading state
        val combinedResponseValue = viewModel.combinedResponse.value

        //Assert
        assertNotNull(combinedResponseValue)
        assertEquals(true, combinedResponseValue.isLoading)
    }
}