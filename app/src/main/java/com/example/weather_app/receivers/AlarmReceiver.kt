package com.example.weather_app.receivers

import com.example.weather_app.data.local.WeatherDatabase
import com.example.weather_app.data.local.WeatherLocalDataSource
import com.example.weather_app.data.remote.RetrofitHelper
import com.example.weather_app.data.remote.WeatherRemoteDataSource
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.utils.Constants
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.weather_app.enums.Units
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private var mediaPlayer: MediaPlayer? = null

        fun stopAlarm(context: Context) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            val manager = NotificationManagerCompat.from(context)
            manager.cancel(1)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(Constants.ALARM_ID, -1)
        Log.i("TAG", "onReceive: $id")

        val repository = WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.apiServices),
            WeatherLocalDataSource(WeatherDatabase.getInstance(context).getWeatherDao())
        )
        removeAlarm(id, repository)
        getData(context, repository)
    }

    private fun removeAlarm(id: Int, repository: WeatherRepository) {
        val result = repository.deleteAlert(id)
        Log.i("TAG", "removeAlarm: $result")
    }

    private fun getData(context: Context, repository: WeatherRepository) {
        GlobalScope.launch(Dispatchers.IO) {
            repository
                .getCurrentWeather(
                    Constants.DEFAULT_LAT,
                    Constants.DEFAULT_LON,
                    Units.METRIC.value,
                    Locale.getDefault().language
                )
        }
    }
}

