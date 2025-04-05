package com.example.weather_app.Localization

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import com.example.weather_app.utils.Constants
import java.util.*

object LanguageManager {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun restartActivity(context: Context) {
        if (context is Activity) {
            val intent = context.intent
            context.finish()
            context.startActivity(intent)
        }
    }

    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(Constants.LANGUAGE_CODE, languageCode).apply()
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString(Constants.LANGUAGE_CODE, "en") ?: "en"
    }
}
