package com.example.jagruk.ui.screens.screenClass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

object JagrukRoutes {
    const val LEARN = "learn"
    const val ALERTS = "alerts"
    const val MY_PLAN = "my_plan"
    const val MODULE = "module/{moduleId}"
    const val MODULE_WITH_ARGS = "module"
    const val EMERGENCY_KIT = "emergency_kit"
    const val SHELTERS = "shelters"
    const val CONTACTS = "contacts"
    const val PROFILE = "profile"
    const val ACHIEVEMENTS = "achievements"
    const val ALL_MODULES = "all_modules"
    const val SETTINGS = "settings"
    const val EMERGENCY_SOS = "emergency_sos"

    fun createModuleRoute(moduleId: String) = "module/$moduleId"
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    object Learn : BottomNavScreen(JagrukRoutes.LEARN, "Learn", Icons.Filled.School)
    object Alerts : BottomNavScreen(JagrukRoutes.ALERTS, "Alerts", Icons.Filled.Notifications)
    object MyPlan : BottomNavScreen(JagrukRoutes.MY_PLAN, "My Plan", Icons.Filled.EditNote)
}