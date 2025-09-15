package com.example.jagruk.ui.screens.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.LearningModule
import com.example.jagruk.data.models.ModuleStatus
import com.example.jagruk.data.models.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor() : ViewModel() {

    private val _learnUiState = MutableStateFlow(LearnUiState())
    val learnUiState: StateFlow<LearnUiState> = _learnUiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _learnUiState.value = _learnUiState.value.copy(
                isLoading = false,
                userProfile = HardcodedData.sampleUser,
                learningModules = HardcodedData.learningModules,
                currentModule = getCurrentModule()
            )
        }
    }

    private fun getCurrentModule(): LearningModule? {
        return HardcodedData.learningModules.find {
            it.status == ModuleStatus.IN_PROGRESS
        }
    }

    fun refreshData() {
        loadUserData()
    }
}

data class LearnUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userProfile: UserProfile? = null,
    val learningModules: List<LearningModule> = emptyList(),
    val currentModule: LearningModule? = null
)