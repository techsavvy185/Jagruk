package com.example.jagruk.ui.screens.shelter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.EmergencyShelter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShelterViewModel @Inject constructor(
    // In a real app, you'd inject repositories here
) : ViewModel() {

    private val _shelterUiState = MutableStateFlow(ShelterUiState())
    val shelterUiState: StateFlow<ShelterUiState> = _shelterUiState.asStateFlow()

    init {
        loadShelters()
    }

    fun refreshShelters() {
        _shelterUiState.update { it.copy(isLoading = true) }
        loadShelters()
    }

    fun selectShelter(shelterId: String) {
        _shelterUiState.update { currentState ->
            currentState.copy(selectedShelterId = shelterId)
        }
    }

    private fun loadShelters() {
        viewModelScope.launch {
            try {
                // Simulate network call
                kotlinx.coroutines.delay(1000)

                _shelterUiState.update {
                    it.copy(
                        isLoading = false,
                        shelters = HardcodedData.emergencyShelters.sortedBy { shelter -> shelter.distanceKm },
                        userLocation = "Indore, Madhya Pradesh",
                        error = null
                    )
                }
            } catch (e: Exception) {
                _shelterUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load shelters"
                    )
                }
            }
        }
    }
}

data class ShelterUiState(
    val isLoading: Boolean = true,
    val shelters: List<EmergencyShelter> = emptyList(),
    val userLocation: String = "",
    val selectedShelterId: String? = null,
    val error: String? = null
)