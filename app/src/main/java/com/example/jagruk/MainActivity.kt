package com.example.jagruk

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jagruk.emergency.EmergencySOSScreen
import com.example.jagruk.ui.screens.alerts.AlertsScreen
import com.example.jagruk.ui.screens.contacts.EmergencyContactsScreen
import com.example.jagruk.ui.screens.emergencyKit.EmergencyKitScreen
import com.example.jagruk.ui.screens.learn.LearnScreen
import com.example.jagruk.ui.screens.module.ModuleScreen
import com.example.jagruk.ui.screens.myPlan.MyPlanScreen
import com.example.jagruk.ui.screens.shelter.ShelterScreen
import com.example.jagruk.ui.theme.JagrukTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.rememberCoroutineScope

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

// Navigation routes
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagrukApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Only show bottom bar on main screens
            val showBottomBar = currentDestination?.route in listOf(
                JagrukRoutes.LEARN,
                JagrukRoutes.ALERTS,
                JagrukRoutes.MY_PLAN
            )

            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    items = listOf(
                        BottomNavScreen.Learn,
                        BottomNavScreen.Alerts,
                        BottomNavScreen.MyPlan
                    )
                )
            }
        }
    ) { innerPadding ->
        JagrukNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onPhoneCall = { phoneNumber ->
                // Handle phone calls
                try {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phoneNumber")
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Handle error - could show a toast or snackbar
                }
            }
        )
    }
}

@Composable
fun JagrukNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onPhoneCall: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = JagrukRoutes.LEARN,
        modifier = modifier
    ) {
        // Main bottom navigation screens
        composable(JagrukRoutes.LEARN) {
            LearnScreen(
                onNavigateToModule = { moduleId ->
                    navController.navigate(JagrukRoutes.createModuleRoute(moduleId))
                },
                onNavigateToEmergencyKit = {
                    navController.navigate(JagrukRoutes.EMERGENCY_KIT)
                },
                onNavigateToContacts = {
                    navController.navigate(JagrukRoutes.CONTACTS)
                },
                onNavigateToShelters = {
                    navController.navigate(JagrukRoutes.SHELTERS)
                },
                navigateToSOS = {
                    navController.navigate("emergency_sos")
                }
            )
        }

        composable(JagrukRoutes.ALERTS) {
            AlertsScreen()
        }

        composable(JagrukRoutes.MY_PLAN) {
            MyPlanScreen(
                onNavigateToEmergencyKit = {
                    navController.navigate(JagrukRoutes.EMERGENCY_KIT)
                },
                onNavigateToContacts = {
                    navController.navigate(JagrukRoutes.CONTACTS)
                },
                onNavigateToShelters = {
                    navController.navigate(JagrukRoutes.SHELTERS)
                }
            )
        }

        // Module learning screen
        composable(
            route = JagrukRoutes.MODULE,
            arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
            ModuleScreen(
                moduleId = moduleId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Emergency Kit screen
        composable(JagrukRoutes.EMERGENCY_KIT) {
            EmergencyKitScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Shelter Locator screen
        composable(JagrukRoutes.SHELTERS) {
            ShelterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Emergency Contacts screen
        composable(JagrukRoutes.CONTACTS) {
            EmergencyContactsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Additional hamburger menu screens (for future expansion)
        composable(JagrukRoutes.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(JagrukRoutes.ACHIEVEMENTS) {
            AchievementsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(JagrukRoutes.ALL_MODULES) {
            AllModulesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToModule = { moduleId ->
                    navController.navigate(JagrukRoutes.createModuleRoute(moduleId))
                }
            )
        }

        composable(JagrukRoutes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Emergency SOS Test Screen
        composable(JagrukRoutes.EMERGENCY_SOS) {
            EmergencySOSScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavScreen>
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
                    if (currentDestination?.route != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

// Placeholder screens for hamburger menu items (you can implement these later)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Profile Screen - Coming Soon!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievements") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Achievements Screen - Coming Soon!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllModulesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToModule: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Modules") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("All Modules Screen - Coming Soon!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Settings Screen - Coming Soon!")
        }
    }
}
