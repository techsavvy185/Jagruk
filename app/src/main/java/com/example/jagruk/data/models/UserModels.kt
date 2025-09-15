package com.example.jagruk.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val location: String,
    val registrationDate: Long,
    val progress: UserProgress
)

data class UserProgress(
    val completedModules: Set<String>,
    val moduleProgress: Map<String, Int>, // moduleId to current page index
    val earnedBadges: List<Badge>,
    val totalScore: Int,
    val streak: Int
)

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: ImageVector,
    val earnedDate: Long
)