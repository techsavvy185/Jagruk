package com.example.jagruk.data.models

data class EmergencyKit(
    val id: String,
    val name: String,
    val items: List<EmergencyKitItem>,
    val isCompleted: Boolean,
    val lastUpdated: Long
)

data class EmergencyKitItem(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val isChecked: Boolean,
    val isPriority: Boolean
)

data class EmergencyContact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val relationship: String,
    val isPrimary: Boolean
)

data class FamilyPlan(
    val id: String,
    val meetingPoints: List<MeetingPoint>,
    val emergencyContacts: List<EmergencyContact>,
    val importantDocuments: List<String>,
    val lastUpdated: Long
)

data class MeetingPoint(
    val id: String,
    val name: String,
    val address: String,
    val type: String, // "Primary", "Secondary"
    val coordinates: Pair<Double, Double>? = null
)