package com.example.jagruk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

// Navigation arguments
object NavigationArgs {
    const val MODULE_ID = "moduleId"
    const val ALERT_ID = "alertId"
    const val CONTACT_ID = "contactId"
}

// Navigation extensions for type safety
fun NavHostController.navigateToModule(moduleId: String) {
    this.navigate("module/$moduleId")
}

fun NavHostController.navigateToEmergencyKit() {
    this.navigate("emergency_kit")
}

fun NavHostController.navigateToShelters() {
    this.navigate("shelters")
}

fun NavHostController.navigateToContacts() {
    this.navigate("contacts")
}

fun NavHostController.navigateToProfile() {
    this.navigate("profile")
}

fun NavHostController.navigateToSettings() {
    this.navigate("settings")
}

fun NavHostController.navigateToAchievements() {
    this.navigate("achievements")
}

fun NavHostController.navigateToAllModules() {
    this.navigate("all_modules")
}

// Safe pop back stack
fun NavHostController.safePopBackStack(): Boolean {
    return if (this.previousBackStackEntry != null) {
        this.popBackStack()
    } else {
        false
    }
}

// Navigate and clear back stack (useful for logout scenarios)
fun NavHostController.navigateAndClearBackStack(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateAndClearBackStack.graph.startDestinationId) {
            inclusive = true
        }
    }
}

// Check if currently on route
fun NavHostController.isCurrentRoute(route: String): Boolean {
    return this.currentDestination?.route == route
}

// Get current route safely
fun NavHostController.getCurrentRoute(): String? {
    return this.currentDestination?.route
}