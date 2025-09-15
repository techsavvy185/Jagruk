package com.example.jagruk.data.models

data class EmergencyShelter(
    val id: String,
    val name: String,
    val address: String,
    val coordinates: Pair<Double, Double>,
    val capacity: Int,
    val facilities: List<String>,
    val contactNumber: String,
    val distanceKm: Double,
    val isGovernmentApproved: Boolean
)