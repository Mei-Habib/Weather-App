package com.example.weather_app

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weather_app.receivers.AlarmReceiver
import com.example.weather_app.utils.Constants

class WeatherWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {

        return Result.success()
    }
}

@SuppressLint("LaunchActivityFromNotification")
fun createWeatherNotification(context: Context, description: String) {
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val dismissIntent = Intent(context, AlarmReceiver::class.java).apply {
        action = Constants.ACTION_DISMISS
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    val dismissPendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        dismissIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val snoozePendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.ACTION_SNOOZE
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(
                Constants.CHANNEL_ID,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
        notificationManager.createNotificationChannel(channel)
    }

    // Notification
    val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
        .setContentTitle("Weather Alert")
        .setContentText(description)
        .setFullScreenIntent(pendingIntent, true)
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.ic_weather)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(Notification.DEFAULT_VIBRATE)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        .addAction(R.drawable.ic_add_alert, "Dismiss", dismissPendingIntent)
        .addAction(R.drawable.ic_add_alert, "Snooze", snoozePendingIntent)


    notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())
}
