package com.example.jagruk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jagruk.ui.screens.alerts.AlertsScreen
import com.example.jagruk.ui.screens.contacts.EmergencyContactsScreen
import com.example.jagruk.ui.screens.emergencyKit.EmergencyKitScreen
import com.example.jagruk.ui.screens.learn.LearnScreen
import com.example.jagruk.ui.screens.module.ModuleScreen
import com.example.jagruk.ui.screens.myPlan.MyPlanScreen
import com.example.jagruk.ui.screens.shelter.ShelterScreen
import com.example.jagruk.ui.theme.JagrukTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JagrukTheme {
                JagrukApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Learn : Screen("learn", "Learn", Icons.Filled.School)
    object Alerts : Screen("alerts", "Alerts", Icons.Filled.Notifications)
    object MyPlan : Screen("my_plan", "My Plan", Icons.Filled.EditNote)
    object Module : Screen("module", "Module", Icons.Filled.MenuBook)
    object EmergencyKit : Screen("emergency_kit", "Emergency Kit", Icons.Filled.Inventory)
    object Shelters : Screen("shelters", "Shelters", Icons.Filled.Home)
    object Contacts : Screen("contacts", "Contacts", Icons.Filled.ContactPhone)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagrukApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = listOf(
                    Screen.Learn,
                    Screen.Alerts,
                    Screen.MyPlan
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Learn.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Learn.route) {
                LearnScreen(
                    onNavigateToModule = { moduleId ->
                        navController.navigate("${Screen.Module.route}/$moduleId")
                    },
                    onNavigateToEmergencyKit = {
                        navController.navigate(Screen.EmergencyKit.route)
                    },
                    onNavigateToContacts = {
                        navController.navigate(Screen.Contacts.route)
                    },
                    onNavigateToShelters = {
                        navController.navigate(Screen.Shelters.route)
                    }
                )
            }

            composable(Screen.Alerts.route) {
                AlertsScreen()
            }

            composable(Screen.MyPlan.route) {
                MyPlanScreen(
                    onNavigateToEmergencyKit = {
                        navController.navigate(Screen.EmergencyKit.route)
                    },
                    onNavigateToContacts = {
                        navController.navigate(Screen.Contacts.route)
                    },
                    onNavigateToShelters = {
                        navController.navigate(Screen.Shelters.route)
                    }
                )
            }

            composable("${Screen.Module.route}/{moduleId}") { backStackEntry ->
                val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                ModuleScreen(
                    moduleId = moduleId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.EmergencyKit.route) {
                EmergencyKitScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Shelters.route) {
                ShelterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Contacts.route) {
                EmergencyContactsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: androidx.navigation.NavController,
    items: List<Screen>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
