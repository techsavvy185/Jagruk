package com.example.jagruk

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jagruk.emergency.EmergencySOSScreen
import com.example.jagruk.navigation.HamburgerMenuDrawer
import com.example.jagruk.ui.screens.achievements.AchievementsScreen
import com.example.jagruk.ui.screens.alerts.AlertsScreen
import com.example.jagruk.ui.screens.allModules.AllModulesScreen
import com.example.jagruk.ui.screens.contacts.EmergencyContactsScreen
import com.example.jagruk.ui.screens.emergencyKit.EmergencyKitScreen
import com.example.jagruk.ui.screens.learn.LearnScreen
import com.example.jagruk.ui.screens.module.ModuleScreen
import com.example.jagruk.ui.screens.myPlan.MyPlanScreen
import com.example.jagruk.ui.screens.profile.ProfileScreen
import com.example.jagruk.ui.screens.screenClass.JagrukRoutes
import com.example.jagruk.ui.screens.settings.SettingsScreen
import com.example.jagruk.ui.screens.shelter.ShelterScreen
import com.example.jagruk.ui.theme.JagrukTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private val REQUEST_OVERLAY_PERMISSION = 1234

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
        }
        enableEdgeToEdge()
        setContent {
            JagrukTheme {
                JagrukApp()
            }
        }
    }
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    "Unable to request overlay permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JagrukApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = JagrukRoutes.LEARN
    ) {
        listOf(JagrukRoutes.LEARN, JagrukRoutes.ALERTS, JagrukRoutes.MY_PLAN).forEach { route ->
            composable(route) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            HamburgerMenuDrawer(
                                navController = navController,
                                onCloseDrawer = {
                                    if (drawerState.isOpen) {
                                        scope.launch { drawerState.close() }
                                    }
                                }
                            )
                        }
                    }
                ) {
                    when (route) {
                        JagrukRoutes.LEARN -> LearnScreen(
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
                                navController.navigate(JagrukRoutes.EMERGENCY_SOS)
                            },
                            navController = navController,
                            openDrawer = {
                                scope.launch { drawerState.open() }
                            }
                        )

                        JagrukRoutes.ALERTS -> AlertsScreen(
                            navController = navController,
                            openDrawer = { scope.launch { drawerState.open() } }
                        )

                        JagrukRoutes.MY_PLAN -> MyPlanScreen(
                            onNavigateToEmergencyKit = {
                                navController.navigate(JagrukRoutes.EMERGENCY_KIT)
                            },
                            onNavigateToContacts = {
                                navController.navigate(JagrukRoutes.CONTACTS)
                            },
                            onNavigateToShelters = {
                                navController.navigate(JagrukRoutes.SHELTERS)
                            },
                            navController = navController,
                            openDrawer = { scope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        }

        // Non-drawer screens
        composable(JagrukRoutes.MODULE) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
            ModuleScreen(
                moduleId = moduleId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(JagrukRoutes.EMERGENCY_KIT) {
            EmergencyKitScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(JagrukRoutes.SHELTERS) {
            ShelterScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(JagrukRoutes.CONTACTS) {
            EmergencyContactsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(JagrukRoutes.PROFILE) {
            ProfileScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(JagrukRoutes.ACHIEVEMENTS) {
            AchievementsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(JagrukRoutes.ALL_MODULES) {
            AllModulesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToModule = { moduleId ->
                    navController.navigate(
                        JagrukRoutes.createModuleRoute(
                            moduleId
                        )
                    )
                }
            )
        }

        composable(JagrukRoutes.SETTINGS) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(JagrukRoutes.EMERGENCY_SOS) {
            EmergencySOSScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}


