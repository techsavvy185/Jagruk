package com.example.jagruk.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.Alert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    // In a real app, you'd inject repositories here
) : ViewModel() {

    private val _alertsUiState = MutableStateFlow(AlertsUiState())
    val alertsUiState: StateFlow<AlertsUiState> = _alertsUiState.asStateFlow()

    init {
        loadAlerts()
    }

    fun refreshAlerts() {
        _alertsUiState.value = _alertsUiState.value.copy(isLoading = true)
        loadAlerts()
    }

    fun markAlertAsRead(alertId: String) {
        // In a real app, this would update the backend
        // For now, we'll just simulate the action
        viewModelScope.launch {
            // Simulate network call delay
            kotlinx.coroutines.delay(500)
        }
    }

    private fun loadAlerts() {
        viewModelScope.launch {
            try {
                // Simulate network call
                kotlinx.coroutines.delay(1000)

                _alertsUiState.value = _alertsUiState.value.copy(
                    isLoading = false,
                    alerts = HardcodedData.currentAlerts.sortedByDescending { it.timestamp },
                    error = null
                )
            } catch (e: Exception) {
                _alertsUiState.value = _alertsUiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load alerts"
                )
            }
        }
    }
}

data class AlertsUiState(
    val isLoading: Boolean = true,
    val alerts: List<Alert> = emptyList(),
    val error: String? = null
)