package com.example.jagruk.ui.screens.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jagruk.data.HardcodedData
import com.example.jagruk.data.models.LearningModule
import com.example.jagruk.data.models.PageType
import com.example.jagruk.data.models.Quiz
import com.example.jagruk.data.models.QuizResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModuleViewModel @Inject constructor() : ViewModel() {

    private val _moduleUiState = MutableStateFlow(ModuleUiState())
    val moduleUiState: StateFlow<ModuleUiState> = _moduleUiState.asStateFlow()

    fun loadModule(moduleId: String) {
        viewModelScope.launch {
            _moduleUiState.update { it.copy(isLoading = true) }

            val module = HardcodedData.learningModules.find { it.id == moduleId }

            _moduleUiState.update {
                it.copy(
                    isLoading = false,
                    module = module,
                    error = if (module == null) "Module not found" else null
                )
            }
        }
    }

    fun selectAnswer(questionId: String, answerIndex: Int) {
        _moduleUiState.update { currentState ->
            currentState.copy(
                selectedAnswers = currentState.selectedAnswers + (questionId to answerIndex)
            )
        }
    }

    fun nextQuestion() {
        _moduleUiState.update { currentState ->
            val currentQuiz = getCurrentQuiz(currentState) ?: return@update currentState
            val nextIndex = (currentState.currentQuestionIndex + 1).coerceAtMost(currentQuiz.questions.size - 1)

            currentState.copy(currentQuestionIndex = nextIndex)
        }
    }

    fun previousQuestion() {
        _moduleUiState.update { currentState ->
            val prevIndex = (currentState.currentQuestionIndex - 1).coerceAtLeast(0)
            currentState.copy(currentQuestionIndex = prevIndex)
        }
    }

    fun submitQuiz() {
        _moduleUiState.update { currentState ->
            val currentQuiz = getCurrentQuiz(currentState) ?: return@update currentState

            val result = calculateQuizResult(currentQuiz, currentState.selectedAnswers)

            currentState.copy(
                showQuizResults = true,
                quizResult = result
            )
        }
    }

    fun retakeQuiz() {
        _moduleUiState.update { currentState ->
            currentState.copy(
                currentQuestionIndex = 0,
                selectedAnswers = emptyMap(),
                showQuizResults = false,
                quizResult = null
            )
        }
    }

    private fun getCurrentQuiz(state: ModuleUiState): Quiz? {
        val currentModule = state.module ?: return null
        // Find the current quiz page - in a real app this would be more sophisticated
        return currentModule.pages.firstOrNull { it.type == PageType.QUIZ }?.quiz
    }

    private fun calculateQuizResult(quiz: Quiz, selectedAnswers: Map<String, Int>): QuizResult {
        var correctCount = 0

        quiz.questions.forEach { question ->
            val selectedAnswer = selectedAnswers[question.id]
            if (selectedAnswer == question.correctAnswerIndex) {
                correctCount++
            }
        }

        val score = correctCount.toDouble() / quiz.questions.size.toDouble()
        val passed = score >= quiz.passingScore

        return QuizResult(
            totalQuestions = quiz.questions.size,
            correctAnswers = correctCount,
            score = score,
            passed = passed,
            answeredQuestions = selectedAnswers
        )
    }
}

data class ModuleUiState(
    val isLoading: Boolean = true,
    val module: LearningModule? = null,
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<String, Int> = emptyMap(),
    val showQuizResults: Boolean = false,
    val quizResult: QuizResult? = null,
    val error: String? = null
)