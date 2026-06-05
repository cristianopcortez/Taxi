package br.com.ccortez.feature.taxi_travel_available_riders.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ccortez.core.common.UiEvents
import br.com.ccortez.core.datastore.UserPreferencesRepository
import br.com.ccortez.feature.taxi_travel_available_riders.domain.usecase.GetTripHistoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val getTripHistoryListUseCase: GetTripHistoryListUseCase,
    _savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _tripHistory = mutableStateOf(CustomerStateHolder())
    val tripHistory: State<CustomerStateHolder> get() = _tripHistory

    private val _query = MutableStateFlow("")
    private val _userId = MutableStateFlow("")
    private val _driverId = MutableStateFlow("")

    val userId: StateFlow<String> get() = _userId
    val driverId: StateFlow<String> get() = _driverId

    init {
        viewModelScope.launch {
            _userId.value = userPreferencesRepository.lastUserId.first()
            _driverId.value = userPreferencesRepository.lastDriverId.first()
        }
    }

    fun setQueryUserId(userId: String) {
        _userId.value = userId
    }

    fun setQueryDriverId(driverId: String) {
        _driverId.value = driverId
    }

    fun setQuery(query: String) {
        _query.value = query
        viewModelScope.launch {
            userPreferencesRepository.saveLastUserId(_userId.value)
            userPreferencesRepository.saveLastDriverId(_driverId.value)
        }
        getTripHistoryList(_userId.value, _driverId.value)
    }

    private fun getTripHistoryList(userId: String, driverId: String) {
        viewModelScope.launch {
            getTripHistoryListUseCase(userId, driverId).collect { uiEvent ->
                when (uiEvent) {
                    is UiEvents.Loading -> _tripHistory.value = CustomerStateHolder(isLoading = true)
                    is UiEvents.Error -> _tripHistory.value = CustomerStateHolder(error = uiEvent.message.toString())
                    is UiEvents.Success -> {
                        _tripHistory.value = CustomerStateHolder(
                            data = uiEvent.data?.rides?.filter { ride ->
                                ride.driver.id.toString().contains(driverId)
                            }
                        )
                    }
                }
            }
        }
    }
}
