package com.example.jagruk.data.models

data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val type: AlertType,
    val severity: AlertSeverity,
    val location: String,
    val timestamp: Long,
    val isActive: Boolean,
    val actionItems: List<String>
)

enum class AlertType {
    EARTHQUAKE,
    LANDSLIDE,
    FLOOD,
    CYCLONE,
    HEAT_WAVE,
    GENERAL
}

enum class AlertSeverity {
    LOW,
    MODERATE,
    HIGH,
    CRITICAL
}
