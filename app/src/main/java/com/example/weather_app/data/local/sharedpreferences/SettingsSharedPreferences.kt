package com.example.weather_app.data.local.sharedpreferences

import android.content.Context
import com.example.weather_app.utils.Constants

class SettingsSharedPreferences private constructor(context: Context) : ISettingsSharedPreferences {
    private val sharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: SettingsSharedPreferences? = null

        fun getInstance(context: Context): SettingsSharedPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsSharedPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun saveSetting(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getSetting(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }
}