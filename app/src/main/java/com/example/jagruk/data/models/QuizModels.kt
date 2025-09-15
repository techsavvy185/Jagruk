package com.example.jagruk.data.models

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String? = null
)

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Double,
    val passed: Boolean,
    val answeredQuestions: Map<String, Int> // questionId to selectedAnswerIndex
)
