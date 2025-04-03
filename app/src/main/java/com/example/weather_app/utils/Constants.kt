package com.example.weather_app.utils

import com.example.weather_app.BuildConfig


object Constants {
    const val API_KEY = BuildConfig.API_KEY
    const val BASE_URL = "https://api.openweathermap.org/"
    const val ALARM_ID = "alarm_id"
    const val LOC_LAT = "loc_lat"
    const val LOC_LON = "loc_lon"
    const val DEFAULT_LAT = 30.9815774
    const val DEFAULT_LON = 30.6164948
    const val NOTIFICATION_ID = 1001
    const val CHANNEL_ID = "channel_id"
    const val REQUEST_CODE = 0
    const val ACTION_DISMISS = "dismiss"
    const val ACTION_SNOOZE = "snooze"
}