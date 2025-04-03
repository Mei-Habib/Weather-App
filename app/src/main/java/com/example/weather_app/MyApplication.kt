package com.example.weather_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.example.weather_app.utils.Constants

@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(context: Context) {

    val channel = NotificationChannel(
        Constants.CHANNEL_ID,
        "Weather Alerts",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Notifies about weather alerts"
    }
    val notificationManager =
        getSystemService(context, NotificationManager::class.java) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

