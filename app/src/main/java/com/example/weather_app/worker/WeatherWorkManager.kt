package com.example.weather_app.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weather_app.R
import com.example.weather_app.data.local.room.WeatherDatabase
import com.example.weather_app.data.local.WeatherLocalDataSource
import com.example.weather_app.data.local.sharedpreferences.SettingsSharedPreferences
import com.example.weather_app.data.remote.RetrofitHelper
import com.example.weather_app.data.remote.WeatherRemoteDataSource
import com.example.weather_app.enums.Units
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.models.WeatherAlert.Companion.toJson
import com.example.weather_app.models.WeatherResponse
import com.example.weather_app.receivers.AlarmReceiver
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.utils.Constants
import com.example.weather_app.utils.getLocationFromAddress

class WeatherWorkManager(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val data = inputData.getString("alert")
        val alert = WeatherAlert.fromJson(data)
        val address = alert.address
        val latLng = getLocationFromAddress(context, address)
        var weather: WeatherResponse? = null
        try {

            val repo = WeatherRepository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.apiServices),
                WeatherLocalDataSource(WeatherDatabase.getInstance(context).getWeatherDao(), SettingsSharedPreferences.getInstance(context))
            )
            latLng?.let {
                weather = repo.getCurrentWeather(
                    Constants.DEFAULT_LAT,
                    Constants.DEFAULT_LON,
                    Units.METRIC.toString(),
                    "en"
                )
            }

            createWeatherNotification(context, weather, alert)

        } catch (ex: Exception) {
            Log.e("TAG", "doWork: ${ex.message}")
            return Result.failure()
        }

        return Result.success()
    }
}

@SuppressLint("LaunchActivityFromNotification")
fun createWeatherNotification(context: Context, weather: WeatherResponse?, alert: WeatherAlert) {
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
        action = Constants.ACTION_STOP
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    val stopPendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        stopIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val snoozePendingIntent = PendingIntent.getBroadcast(
        context,
        Constants.REQUEST_CODE,
        Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.ACTION_SNOOZE
            putExtra("alert", alert.toJson())
        },
        PendingIntent.FLAG_IMMUTABLE
    )

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel =
            NotificationChannel(
                Constants.CHANNEL_ID,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(soundUri, audioAttributes)
                enableVibration(true)
            }
        notificationManager.createNotificationChannel(channel)
    }

    // Notification
    val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Weather Alert: ${weather?.weather?.firstOrNull()?.description}")
        .setContentText("Temperature: ${weather?.main?.temp}°C")
        .setFullScreenIntent(pendingIntent, true)
        .setContentIntent(pendingIntent)
        .setAutoCancel(false)
        .setOngoing(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setDefaults(Notification.DEFAULT_VIBRATE)
        .addAction(R.drawable.ic_add_alert, "Snooze", snoozePendingIntent)
        .addAction(R.drawable.ic_add_alert, "Stop", stopPendingIntent)


    notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())
}
