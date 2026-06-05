package br.com.ccortez.feature.taxi_travel_options.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetRideConfirmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class RequestRideViewModel @Inject constructor(
    private val getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase,
    private val getRideConfirmUseCase: GetRideConfirmUseCase,
) : ViewModel() {

    private val _combinedResponse = mutableStateOf(CombinedStateHolder())
    val combinedResponse: State<CombinedStateHolder> get() = _combinedResponse

    private val _rideConfirmResponse = mutableStateOf(RideConfirmStateHolder())
    val rideConfirmResponse: State<RideConfirmStateHolder> get() = _rideConfirmResponse

    private var _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String> get() = _userId
    private var _originAddress: MutableStateFlow<String> = MutableStateFlow("")
    val originAddress: StateFlow<String> get() = _originAddress
    private var _destinyAddress: MutableStateFlow<String> = MutableStateFlow("")
    val destinyAddress: StateFlow<String> get() = _destinyAddress

    fun getCombinedData(userId: String, originAddress: String, destinyAddress: String) = viewModelScope.launch {
        getAvailableRidersAndRouteResponseUseCase(userId, originAddress, destinyAddress).onEach {
            when (it) {
                is UiEvents.Loading -> {
                    _combinedResponse.value = CombinedStateHolder(isLoading = true)
                }

                is UiEvents.Error -> {
                    _combinedResponse.value = CombinedStateHolder(error = it.message.toString())
                }

                is UiEvents.Success -> {
                    _combinedResponse.value = CombinedStateHolder(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setQuery(userId: String, originAddress: String, destinyAddress: String) {
        viewModelScope.launch {
            getCombinedData(userId, originAddress, destinyAddress)
        }
    }

    fun setQuery(_s: String) {
        viewModelScope.launch {
            getCombinedData(userId.value, originAddress.value, destinyAddress.value)
        }
    }

    fun setQueryUserId(s: String) {
        _userId.value = s
    }

    fun setQueryOriginAddress(s: String) {
        _originAddress.value = s
    }

    fun setQueryDestinyAddress(s: String) {
        _destinyAddress.value = s
    }

    fun rideConfirm(customerId: String, originAddress: String, destinyAddress: String,
                    distance: Int, duration: String, driverId: Int, driverName: String,
                    rideValue: Double) = viewModelScope.launch {
        getRideConfirmUseCase(customerId, originAddress, destinyAddress, distance, duration,
            driverId, driverName, rideValue).onEach {
            when (it) {
                is UiEvents.Loading -> {
                    _rideConfirmResponse.value = RideConfirmStateHolder(isLoading = true)
                }

                is UiEvents.Error -> {
                    _rideConfirmResponse.value = RideConfirmStateHolder(error = it.message.toString())
                }

                is UiEvents.Success -> {

                    _rideConfirmResponse.value =
                        RideConfirmStateHolder(data = it.data)

                }
            }
        }.launchIn(viewModelScope)
    }

}