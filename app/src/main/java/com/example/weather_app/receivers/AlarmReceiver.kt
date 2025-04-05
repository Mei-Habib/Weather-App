package com.example.weather_app.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import com.example.weather_app.utils.Constants
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weather_app.R
import com.example.weather_app.worker.WeatherWorkManager
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.utils.formatTime
import com.example.weather_app.utils.scheduleWeatherAlert
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            when (intent?.action) {
                Constants.ACTION_STOP -> {
                    val notificationManager =
                        context.getSystemService(NotificationManager::class.java)

                    notificationManager?.cancel(Constants.NOTIFICATION_ID)
                }

                Constants.ACTION_SNOOZE -> {
                    val alertJson = intent.getStringExtra("alert")
                    val alert = alertJson?.let { WeatherAlert.fromJson(it) }
                    val workManager = WorkManager.getInstance(context)

                    if (alert != null) {
                        snoozeAlert(workManager, alert, 1, context)
                    } else {

                    }
                }

                else -> {}
            }
        }

    }
}

private fun scheduleSnooze(context: Context, triggerAtMillis: Long) {
    val delay = triggerAtMillis - System.currentTimeMillis()

    val workRequest = OneTimeWorkRequestBuilder<WeatherWorkManager>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

@RequiresApi(Build.VERSION_CODES.O)
fun snoozeAlert(workManager: WorkManager, alert: WeatherAlert, snoozeMinutes: Int=10, context: Context) {
    val snoozedStartTime = System.currentTimeMillis() + snoozeMinutes * 60 * 1000

    val snoozedAlert = alert.copy(
        startDuration = snoozedStartTime.toString()
    )

    scheduleWeatherAlert(workManager, snoozedAlert)

    // Update the notification with the new snooze time
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    val snoozeNotification = NotificationCompat.Builder(context, "weather_alerts")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Weather Alert Snoozed")
        .setContentText("Alert will ring again at ${formatTime(snoozeMinutes.toLong())}")
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(true) // Keep it in notifications
        .addAction(R.drawable.ic_add_alert, "Stop", getStopIntent(context, alert.id))
        .build()

    notificationManager.notify(alert.id, snoozeNotification)
}

private fun getStopIntent(context: Context, alertId: Int): PendingIntent {
    val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
        action = "ACTION_DISMISS"
        putExtra("alert_id", alertId)
    }
    return PendingIntent.getBroadcast(
        context,
        alertId,
        stopIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

//fun playAlarmSoundAndVibrate(context: Context) {
//    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//    val ringtonePlayer = RingtoneManager.getRingtone(context, ringtone)
//
//    // Start sound
//    ringtonePlayer.play()
//
//    // Start vibration
//    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        // For Android Oreo and above, use Vibrator class with vibration pattern
//        vibrator.vibrate(
//            VibrationEffect.createWaveform(
//                longArrayOf(0, 500, 200, 500),
//                -1
//            )
//        ) // -1 means no repeat
//    } else {
//        // For older versions
////        vibrator.vibrate(longArrayOf(0, 500, 200, 500))
//    }
//
////    showNotification(context)
//}