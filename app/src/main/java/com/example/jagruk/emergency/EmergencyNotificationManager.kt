package com.example.jagruk.emergency

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.jagruk.MainActivity
import com.example.jagruk.data.models.Alert
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val EMERGENCY_CHANNEL_ID = "emergency_alerts"
        private const val EMERGENCY_CHANNEL_NAME = "Emergency Alerts"
        private const val EMERGENCY_NOTIFICATION_ID = 2001

        // Use companion object to maintain MediaPlayer reference
        @Volatile
        private var mediaPlayer: MediaPlayer? = null
        private val TAG = "EmergencyAlert"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                EMERGENCY_CHANNEL_ID,
                EMERGENCY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical emergency alerts and warnings"
                enableLights(true)
                lightColor = android.graphics.Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000, 500, 1000, 500, 1000)
                setBypassDnd(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showEmergencyNotification(alert: Alert) {
        android.util.Log.d(TAG, "showEmergencyNotification called")

        // Start full screen activity
        startEmergencyActivity(alert)

        // Play alarm sound
        playEmergencySound()

        // Vibrate device
        vibrateDevice()

        // Show heads-up notification
        showHeadsUpNotification(alert)
    }

    private fun startEmergencyActivity(alert: Alert) {
        val intent = Intent(context, EmergencyAlertActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_USER_ACTION
            putExtra("alert_data", alert)
        }
        context.startActivity(intent)
    }

    private fun playEmergencySound() {
        android.util.Log.d(TAG, "playEmergencySound called")

        synchronized(EmergencyNotificationManager::class.java) {
            try {
                // Stop any existing sound first
                stopSoundInternal()

                android.util.Log.d(TAG, "Creating new MediaPlayer")
                mediaPlayer = MediaPlayer().apply {
                    // Use newer AudioAttributes instead of deprecated setAudioStreamType
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        setAudioStreamType(AudioManager.STREAM_ALARM)
                    }

                    setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                    isLooping = true
                    prepare()
                    start()
                    android.util.Log.d(TAG, "MediaPlayer started successfully, instance: $this")
                }
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error playing emergency sound", e)
            }
        }
    }

    private fun stopSoundInternal() {
        android.util.Log.d(TAG, "stopSoundInternal called, mediaPlayer = $mediaPlayer")

        mediaPlayer?.let { mp ->
            try {
                android.util.Log.d(TAG, "MediaPlayer exists: $mp, checking if playing...")

                if (mp.isPlaying) {
                    android.util.Log.d(TAG, "MediaPlayer is playing, stopping...")
                    mp.stop()
                    android.util.Log.d(TAG, "MediaPlayer stopped")
                } else {
                    android.util.Log.d(TAG, "MediaPlayer is not playing")
                }

                android.util.Log.d(TAG, "Releasing MediaPlayer...")
                mp.release()
                android.util.Log.d(TAG, "MediaPlayer released")

            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error stopping MediaPlayer", e)
                // Force release
                try {
                    mp.release()
                    android.util.Log.d(TAG, "MediaPlayer force released")
                } catch (releaseError: Exception) {
                    android.util.Log.e(TAG, "Error force releasing MediaPlayer", releaseError)
                }
            }
        } ?: android.util.Log.d(TAG, "MediaPlayer is null, nothing to stop")

        mediaPlayer = null
        android.util.Log.d(TAG, "MediaPlayer reference set to null")
    }

    private fun vibrateDevice() {
        val vibrationPattern = longArrayOf(0, 1000, 500, 1000, 500, 1000, 500, 1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(vibrationPattern, 0)
            }
        }
    }

    private fun showHeadsUpNotification(alert: Alert) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to_alerts", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, EMERGENCY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("🚨 EMERGENCY ALERT")
            .setContentText(alert.title)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("${alert.title}\n\n${alert.description}"))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setOngoing(true)
            .setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            notificationManager.notify(EMERGENCY_NOTIFICATION_ID, notification)
        }
    }

    fun stopEmergencyAlert() {
        android.util.Log.d(TAG, "stopEmergencyAlert called")

        synchronized(EmergencyNotificationManager::class.java) {
            // Stop sound
            stopSoundInternal()
        }

        // Stop vibration
        android.util.Log.d(TAG, "Stopping vibration...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.cancel()
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.cancel()
        }
        android.util.Log.d(TAG, "Vibration stopped")

        // Cancel notification
        notificationManager.cancel(EMERGENCY_NOTIFICATION_ID)
        android.util.Log.d(TAG, "stopEmergencyAlert completed")
    }
}
