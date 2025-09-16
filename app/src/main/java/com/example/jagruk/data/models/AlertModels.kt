package com.example.jagruk.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable

@Parcelize
enum class AlertType : Parcelable {
    EARTHQUAKE,
    LANDSLIDE,
    FLOOD,
    CYCLONE,
    HEAT_WAVE,
    GENERAL
}

@Parcelize
enum class AlertSeverity : Parcelable {
    LOW,
    MODERATE,
    HIGH,
    CRITICAL
}
