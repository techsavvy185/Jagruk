package com.example.jagruk.emergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.models.Alert
import com.example.jagruk.data.models.AlertSeverity
import com.example.jagruk.data.models.AlertType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EmergencySOSViewModel @Inject constructor(
    private val alarmManager: EmergencyAlarmManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmergencySOSUiState())
    val uiState: StateFlow<EmergencySOSUiState> = _uiState.asStateFlow()

    fun scheduleTestAlert() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScheduling = true)

            val testAlert = Alert(
                id = UUID.randomUUID().toString(),
                title = "🚨 EMERGENCY DRILL - EARTHQUAKE DETECTED",
                description = "This is a test emergency alert. A magnitude 7.2 earthquake has been detected 15km from your location. Take immediate cover using Drop, Cover, Hold On protocol.",
                type = AlertType.EARTHQUAKE,
                severity = AlertSeverity.CRITICAL,
                location = "Indore, Madhya Pradesh",
                timestamp = System.currentTimeMillis(),
                isActive = true,
                actionItems = listOf(
                    "Drop to the ground immediately",
                    "Take cover under a desk or table",
                    "Hold on until shaking stops",
                    "Evacuate only after shaking ends"
                )
            )

            alarmManager.scheduleEmergencyAlert(10, testAlert)

            _uiState.value = _uiState.value.copy(
                isScheduling = false,
                lastScheduledTime = System.currentTimeMillis(),
                message = "Emergency alert scheduled for 10 seconds from now!"
            )
        }
    }

    fun cancelScheduledAlert() {
        viewModelScope.launch {
            alarmManager.cancelEmergencyAlert()
            _uiState.value = _uiState.value.copy(
                message = "Scheduled alert cancelled"
            )
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}

data class EmergencySOSUiState(
    val isScheduling: Boolean = false,
    val lastScheduledTime: Long? = null,
    val message: String? = null
)