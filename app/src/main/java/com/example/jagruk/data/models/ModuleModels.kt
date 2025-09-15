package com.example.jagruk.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class LearningModule(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: ImageVector,
    val status: ModuleStatus,
    val pages: List<ModulePage>,
    val estimatedTimeMinutes: Int,
    val difficulty: String = "Intermediate"
)

data class ModulePage(
    val id: String,
    val title: String,
    val content: String,
    val type: PageType,
    val quiz: Quiz? = null
)

data class Quiz(
    val id: String,
    val title: String,
    val questions: List<QuizQuestion>,
    val passingScore: Double = 0.6
)

enum class PageType {
    CONTENT,
    QUIZ
}

enum class ModuleStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}