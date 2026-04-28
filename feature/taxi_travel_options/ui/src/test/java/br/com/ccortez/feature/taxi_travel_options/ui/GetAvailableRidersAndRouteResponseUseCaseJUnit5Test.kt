package br.com.ccortez.feature.taxi_travel_options.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.core.network.model.RideRequestDto
import br.com.ccortez.feature.taxi_travel_options.data.mapper.toDomainCombined
import br.com.ccortez.feature.taxi_travel_options.domain.model.Combined
import br.com.ccortez.feature.taxi_travel_options.domain.repository.TaxiTravelOptionsRepository
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersListUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetRideConfirmUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.ui.api.FakeApiService
import br.com.ccortez.feature.taxi_travel_options.ui.screen.RequestRideViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.Answer
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@HiltAndroidTest
@Config(sdk = [25], application = HiltTestApplication::class, manifest= Config.NONE)
@ExperimentalCoroutinesApi
@LooperMode(LooperMode.Mode.PAUSED)
@ExtendWith(MockitoExtension::class)
class GetAvailableRidersAndRouteResponseUseCaseJUnit5Test {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockTaxiTravelOptionsRepository: TaxiTravelOptionsRepository

    @Mock
    private lateinit var getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase

    @Mock
    private lateinit var getAvailableRidersListUseCase: GetAvailableRidersListUseCase

    @Mock
    private lateinit var getRouteResponseUseCase: GetRouteResponseUseCase

    @Mock
    private lateinit var getRideConfirmUseCase: GetRideConfirmUseCase

    private lateinit var combined: Combined

    @BindValue
    @JvmField
    val fakeApiService: FakeApiService = FakeApiService()

    private lateinit var requestRideViewModel: RequestRideViewModel

    companion object {

        private val dispatcher = StandardTestDispatcher()

        @JvmStatic
        @BeforeAll
        fun setupClass() {
            // Setup for the class
            Dispatchers.setMain(dispatcher)
        }

        @JvmStatic
        @AfterAll
        fun tearDownClass() {
            // Teardown for the class
            Dispatchers.resetMain()
        }
    }

    @BeforeEach
    fun setup() {

        runTest {

            val userId = "1"
            val originAddress = "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001"
            val destinyAddress = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"

            val rideRequestDto = RideRequestDto(userId, originAddress, destinyAddress)

            combined =
                fakeApiService.getAvailableRidersAndRouteResponse(rideRequestDto).toDomainCombined()

            val expectedFlow = flow {
                emit(UiEvents.Loading())
                emit(UiEvents.Success(combined))
            }

            Mockito.`when`(
                getAvailableRidersAndRouteResponseUseCase.invoke(
                    userId,
                    originAddress,
                    destinyAddress
                )
            )
                .thenReturn(expectedFlow)
            requestRideViewModel = RequestRideViewModel(
                getAvailableRidersAndRouteResponseUseCase,
                getAvailableRidersListUseCase,
                getRideConfirmUseCase
            )
        }
    }

    @Test
    fun testUseCaseFlowEmission() {
        val userId = "1"
        val originAddress = "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001"
        val destinyAddress = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"

        val rideRequestDto = RideRequestDto(userId, originAddress, destinyAddress)

        val flowAnswer = Answer<Flow<UiEvents<Combined>>> { _ ->
            val flow = flow {
                emit(UiEvents.Loading())
                emit(UiEvents.Success(mockTaxiTravelOptionsRepository.getAvailableRidersAndRouteResponse(
                    userId, originAddress, destinyAddress
                )))
            }
            flow
        }

        // Extract the actual Flow<String> from the Answer
        val flowToEmit = flowAnswer.answer(null) // Provide null argument (optional)

        // Configure mock behavior using the Answer
        Mockito.`when`(
            getAvailableRidersAndRouteResponseUseCase.invoke(
                userId, originAddress, destinyAddress)
        ).thenReturn(flowToEmit)

        runTest {

            combined = fakeApiService.getAvailableRidersAndRouteResponse(rideRequestDto).toDomainCombined()

            Mockito.`when`(mockTaxiTravelOptionsRepository.getAvailableRidersAndRouteResponse(
                userId, originAddress, destinyAddress
            )).thenReturn(combined)

            // Use the mock use case in your ViewModel test logic
            requestRideViewModel = RequestRideViewModel(
                getAvailableRidersAndRouteResponseUseCase,
                getAvailableRidersListUseCase,
                getRideConfirmUseCase
            )
            requestRideViewModel.getCombinedData(userId, originAddress, destinyAddress)

            val value = requestRideViewModel.combinedResponse.value

            org.junit.jupiter.api.Assertions.assertNotNull(value.data)

        }
    }

}