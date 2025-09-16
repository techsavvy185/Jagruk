package com.example.jagruk.emergency

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jagruk.MainActivity
import com.example.jagruk.data.models.Alert
import com.example.jagruk.ui.theme.JagrukTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EmergencyAlertActivity : ComponentActivity() {

    @Inject
    lateinit var notificationManager: EmergencyNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize notification manager - REMOVED: notificationManager = EmergencyNotificationManager(this)

        // Make this activity appear over lock screen and turn screen on
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }

        val alertData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("alert_data", Alert::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Alert>("alert_data")
        }

        setContent {
            JagrukTheme {
                EmergencyAlertScreen(
                    alert = alertData,
                    onDismiss = { finishAlert() },
                    onViewDetails = { openMainApp() }
                )
            }
        }
    }

    private fun finishAlert() {
        // Stop sound and vibration before finishing
        notificationManager.stopEmergencyAlert()
        finish()
    }

    private fun openMainApp() {
        // Stop sound and vibration
        notificationManager.stopEmergencyAlert()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to_alerts", true)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure cleanup happens even if activity is destroyed unexpectedly
        // No need to check isInitialized for Hilt injected properties that are lateinit
        // Hilt ensures it's initialized before use in lifecycle methods like onDestroy.
        // However, calling stop here might be redundant if finishAlert or openMainApp already did.
        // But it can serve as a final safety net.
        notificationManager.stopEmergencyAlert()
    }

    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        // Stop sound and vibration when back button is pressed
        super.onBackPressed()
        finishAlert()
    }
    override fun onPause() {
        super.onPause()
        // Stop alert when activity goes to background
        notificationManager.stopEmergencyAlert()
    }

    override fun onStop() {
        super.onStop()
        // Additional safety net
        notificationManager.stopEmergencyAlert()
    }
}

@Composable
fun EmergencyAlertScreen(
    alert: Alert?,
    onDismiss: () -> Unit,
    onViewDetails: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                // Flashing warning icon
                var isVisible by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    while (true) {
                        kotlinx.coroutines.delay(500)
                        isVisible = !isVisible
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isVisible) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Color.Black
                        )
                    } else {
                        Spacer(modifier = Modifier.size(60.dp))
                    }


                    Text(
                        text = "EMERGENCY ALERT",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = alert?.title ?: "Emergency Alert",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Red
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = alert?.description ?: "Emergency alert description",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (!alert?.actionItems.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "IMMEDIATE ACTIONS:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )

                            alert?.actionItems?.forEach { action ->
                                Text(
                                    text = "• $action",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Dismiss", fontWeight = FontWeight.Bold)
                }



            }
        }
    }
}
