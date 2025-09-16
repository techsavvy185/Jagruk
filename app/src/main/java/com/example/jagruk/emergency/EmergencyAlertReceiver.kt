package com.example.jagruk.emergency

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.jagruk.data.models.Alert
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class EmergencyAlertReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "EmergencyAlertReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Emergency alert received")

        try {
            val alertData = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    EmergencyAlarmManager.EXTRA_ALERT_DATA,
                    Alert::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Alert>(EmergencyAlarmManager.EXTRA_ALERT_DATA)
            }

            if (alertData != null) {
                // Create notification manager directly without Hilt
                val notificationManager = EmergencyNotificationManager(context)
                notificationManager.showEmergencyNotification(alertData)

            } else {
                Log.e(TAG, "No alert data found in intent")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing emergency alert", e)
        }
    }
}