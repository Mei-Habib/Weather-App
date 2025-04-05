package com.example.weather_app.data.local.sharedpreferences

interface ISettingsSharedPreferences {
    fun saveSetting(key: String, value: String)
    fun getSetting(key: String, defValue: String): String?
}