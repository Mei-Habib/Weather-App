package com.example.weather_app.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import com.example.weather_app.utils.Constants
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.example.weather_app.R
import com.example.weather_app.enums.AlertType
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            when (intent?.action) {
                Constants.ACTION_DISMISS -> {
                    val notificationManager =
                        context.getSystemService(NotificationManager::class.java)

                    notificationManager?.cancel(Constants.NOTIFICATION_ID)
                }

                Constants.ACTION_SNOOZE -> {
                    val workManager = WorkManager.getInstance(context)

//                    scheduleSnoozedAlert(context, workManager, weatherDetails)
                }

                else -> {}
            }
        }

    }
}


// alarm manager that will trigger broadcast receiver
fun scheduleAlarm(context: Context, startTimeInMillis: Long, alertType: AlertType) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("ALERT_TYPE", alertType.name)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        startTimeInMillis,
        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
        pendingIntent
    )
}

fun dismissNotification(context: Context) {
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    alarmManager.cancel(pendingIntent)
}

//@SuppressLint("LaunchActivityFromNotification")
//fun showNotification(context: Context) {
//    val intent = Intent(context, AlarmReceiver::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(
//        context,
//        Constants.REQUEST_CODE,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val stopIntent = Intent(context, StopAlertReceiver::class.java)
//    val stopPendingIntent = PendingIntent.getBroadcast(
//        context, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val notificationManager = context.getSystemService(NotificationManager::class.java)
//    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//    val vibrationPattern = longArrayOf(0, 500, 200, 500)
//    val notification =
//        NotificationCompat.Builder(context, Constants.CHANNEL_ID)
//            .setContentTitle("Weather Notification")
//            .setContentText("Message or text with notification")
//            .setSmallIcon(R.drawable.sunny)
//            .setPriority(NotificationManager.IMPORTANCE_HIGH)
//            .setFullScreenIntent(pendingIntent, true)
//            .setSound(soundUri)
//            .setVibrate(vibrationPattern)
//            .addAction(R.drawable.ic_undo, "Dismiss", stopPendingIntent)
//            .build()
//
//    notificationManager.notify(Constants.NOTIFICATION_ID, notification)
//}

fun playAlarmSoundAndVibrate(context: Context) {
    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    val ringtonePlayer = RingtoneManager.getRingtone(context, ringtone)

    // Start sound
    ringtonePlayer.play()

    // Start vibration
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // For Android Oreo and above, use Vibrator class with vibration pattern
        vibrator.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 500, 200, 500),
                -1
            )
        ) // -1 means no repeat
    } else {
        // For older versions
//        vibrator.vibrate(longArrayOf(0, 500, 200, 500))
    }

//    showNotification(context)
}