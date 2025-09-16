package com.example.jagruk.emergency

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.jagruk.data.models.Alert
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyAlarmManager @Inject constructor(
    private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        private const val TAG = "EmergencyAlarmManager"
        const val EMERGENCY_ALERT_REQUEST_CODE = 1001
        const val EXTRA_ALERT_DATA = "extra_alert_data"
    }

    fun scheduleEmergencyAlert(delayInSeconds: Int, alertData: Alert) {
        try {
            // Check if we can schedule exact alarms
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                // Request exact alarm permission
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                return
            }

            val intent = Intent(context, EmergencyAlertReceiver::class.java).apply {
                putExtra(EXTRA_ALERT_DATA, alertData)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                EMERGENCY_ALERT_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerTime = System.currentTimeMillis() + (delayInSeconds * 1000L)

            // Use setExactAndAllowWhileIdle for critical emergency alerts
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )

            Log.d(TAG, "Emergency alert scheduled for ${delayInSeconds} seconds")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule emergency alert", e)
        }
    }

    fun cancelEmergencyAlert() {
        try {
            val intent = Intent(context, EmergencyAlertReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                EMERGENCY_ALERT_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "Emergency alert cancelled")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cancel emergency alert", e)
        }
    }
}
