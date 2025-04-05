package com.example.weather_app.utils

import com.example.weather_app.BuildConfig


object Constants {
    const val API_KEY = BuildConfig.API_KEY
    const val BASE_URL = "https://api.openweathermap.org/"
    const val DEFAULT_LAT = 30.9815774
    const val DEFAULT_LON = 30.6164948
    const val NOTIFICATION_ID = 1001
    const val CHANNEL_ID = "channel_id"
    const val REQUEST_CODE = 0
    const val ACTION_STOP = "stop"
    const val ACTION_SNOOZE = "snooze"
    const val SHARED_PREFERENCES_NAME = "shared_preferences"
    const val LANGUAGE_CODE = "language_code"
    const val TEMP_UNIT = "temp_unit"
    const val WIND_SPEED_UNIT = "wind_speed_unit"
    const val LOCATION = "location"
}