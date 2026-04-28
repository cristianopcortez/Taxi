package br.com.ccortez.feature.taxi_travel_options.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersAndRouteResponseUseCase
import br.com.ccortez.feature.taxi_travel_options.domain.usecase.GetAvailableRidersListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TravelOptionsViewModel @Inject constructor(
    private val getAvailableRidersAndRouteResponseUseCase: GetAvailableRidersAndRouteResponseUseCase,
    private val getAvailableRidersListUseCase: GetAvailableRidersListUseCase,
) : ViewModel() {

    private val _combinedResponse = MutableStateFlow(CombinedStateHolder())
    // CORRECT: Expose as StateFlow, not State
    val combinedResponse: StateFlow<CombinedStateHolder> = _combinedResponse.asStateFlow()

    private var _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String> get() = _userId
    private var _originAddress: MutableStateFlow<String> = MutableStateFlow("")
    val originAddress: StateFlow<String> get() = _originAddress
    private var _destinyAddress: MutableStateFlow<String> = MutableStateFlow("")
    val destinyAddress: StateFlow<String> get() = _destinyAddress

    private val addressFlow = combine(_userId, _originAddress, _destinyAddress) { userId, origin, destiny ->
        Triple(userId, origin, destiny)
    }

    init {
        addressFlow
            .onEach { (userId, origin, destiny) ->
                if (userId.isNotBlank() && origin.isNotBlank() && destiny.isNotBlank()) {
                    getCombinedData(userId, origin, destiny)
                }
            }
            .launchIn(viewModelScope)
    }

    // Make this private.  It should only be called from the init block.
    private fun getCombinedData(userId: String, originAddress: String, destinyAddress: String) {
        getAvailableRidersAndRouteResponseUseCase(userId, originAddress, destinyAddress)
            .onEach {
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
            }
            .launchIn(viewModelScope)
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
}