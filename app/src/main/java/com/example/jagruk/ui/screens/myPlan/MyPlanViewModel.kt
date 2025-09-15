package com.example.jagruk.ui.screens.myPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.EmergencyContact
import com.example.jagruk.data.models.EmergencyKit
import com.example.jagruk.data.models.FamilyPlan
import com.example.jagruk.data.models.MeetingPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPlanViewModel @Inject constructor(
    // In a real app, you'd inject repositories here
) : ViewModel() {

    private val _myPlanUiState = MutableStateFlow(MyPlanUiState())
    val myPlanUiState: StateFlow<MyPlanUiState> = _myPlanUiState.asStateFlow()

    init {
        loadPlanData()
    }

    private fun loadPlanData() {
        viewModelScope.launch {
            val emergencyKit = EmergencyKit(
                id = "kit_001",
                name = "Personal Emergency Kit",
                items = HardcodedData.emergencyKitItems,
                isCompleted = false,
                lastUpdated = System.currentTimeMillis()
            )

            val familyPlan = FamilyPlan(
                id = "plan_001",
                meetingPoints = listOf(
                    MeetingPoint(
                        id = "mp_001",
                        name = "Government High School",
                        address = "Scheme 54, Vijay Nagar",
                        type = "Primary"
                    ),
                    MeetingPoint(
                        id = "mp_002",
                        name = "Community Center",
                        address = "Geeta Bhawan Area",
                        type = "Secondary"
                    )
                ),
                emergencyContacts = HardcodedData.emergencyContacts,
                importantDocuments = listOf(
                    "Aadhaar Card", "PAN Card", "Insurance Papers",
                    "Bank Documents", "Medical Records"
                ),
                lastUpdated = System.currentTimeMillis()
            )

            val checkedItems = emergencyKit.items.count { it.isChecked }
            val totalItems = emergencyKit.items.size + 5 // 5 additional preparedness items

            _myPlanUiState.update {
                it.copy(
                    isLoading = false,
                    emergencyKit = emergencyKit,
                    familyPlan = familyPlan,
                    emergencyContacts = HardcodedData.emergencyContacts,
                    completedPreparednessItems = checkedItems,
                    totalPreparednessItems = totalItems
                )
            }
        }
    }

    fun toggleKitItem(itemId: String) {
        viewModelScope.launch {
            _myPlanUiState.update { currentState ->
                val updatedKit = currentState.emergencyKit?.copy(
                    items = currentState.emergencyKit.items.map { item ->
                        if (item.id == itemId) {
                            item.copy(isChecked = !item.isChecked)
                        } else item
                    }
                )

                val newCheckedCount = updatedKit?.items?.count { it.isChecked } ?: 0

                currentState.copy(
                    emergencyKit = updatedKit,
                    completedPreparednessItems = newCheckedCount
                )
            }
        }
    }

    fun addEmergencyContact(contact: EmergencyContact) {
        viewModelScope.launch {
            _myPlanUiState.update { currentState ->
                currentState.copy(
                    emergencyContacts = currentState.emergencyContacts + contact
                )
            }
        }
    }

    fun updateFamilyPlan(plan: FamilyPlan) {
        viewModelScope.launch {
            _myPlanUiState.update { currentState ->
                currentState.copy(
                    familyPlan = plan.copy(lastUpdated = System.currentTimeMillis())
                )
            }
        }
    }
}

data class MyPlanUiState(
    val isLoading: Boolean = true,
    val emergencyKit: EmergencyKit? = null,
    val familyPlan: FamilyPlan? = null,
    val emergencyContacts: List<EmergencyContact> = emptyList(),
    val completedPreparednessItems: Int = 0,
    val totalPreparednessItems: Int = 0,
    val error: String? = null
)