package com.example.jagruk.ui.screens.emergencyKit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.EmergencyKit
import com.example.jagruk.data.models.EmergencyKitItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyKitViewModel @Inject constructor(
    // In a real app, you'd inject repositories here
) : ViewModel() {

    private val _emergencyKitUiState = MutableStateFlow(EmergencyKitUiState())
    val emergencyKitUiState: StateFlow<EmergencyKitUiState> = _emergencyKitUiState.asStateFlow()

    init {
        loadEmergencyKit()
    }

    private fun loadEmergencyKit() {
        viewModelScope.launch {
            val emergencyKit = EmergencyKit(
                id = "kit_001",
                name = "Personal Emergency Kit",
                items = HardcodedData.emergencyKitItems,
                isCompleted = false,
                lastUpdated = System.currentTimeMillis()
            )

            val categories = emergencyKit.items.map { it.category }.distinct().sorted()

            _emergencyKitUiState.update {
                it.copy(
                    isLoading = false,
                    emergencyKit = emergencyKit,
                    categories = categories,
                    filteredItems = emergencyKit.items
                )
            }
        }
    }

    fun toggleKitItem(itemId: String) {
        _emergencyKitUiState.update { currentState ->
            val updatedKit = currentState.emergencyKit?.copy(
                items = currentState.emergencyKit.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(isChecked = !item.isChecked)
                    } else item
                },
                lastUpdated = System.currentTimeMillis()
            )

            val updatedFilteredItems = if (currentState.selectedCategory != null) {
                updatedKit?.items?.filter { it.category == currentState.selectedCategory } ?: emptyList()
            } else {
                updatedKit?.items ?: emptyList()
            }

            currentState.copy(
                emergencyKit = updatedKit,
                filteredItems = updatedFilteredItems
            )
        }
    }

    fun filterByCategory(category: String?) {
        _emergencyKitUiState.update { currentState ->
            val filteredItems = if (category == null) {
                currentState.emergencyKit?.items ?: emptyList()
            } else {
                currentState.emergencyKit?.items?.filter { it.category == category } ?: emptyList()
            }

            currentState.copy(
                selectedCategory = category,
                filteredItems = filteredItems
            )
        }
    }
}

data class EmergencyKitUiState(
    val isLoading: Boolean = true,
    val emergencyKit: EmergencyKit? = null,
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val filteredItems: List<EmergencyKitItem> = emptyList(),
    val error: String? = null
)