package com.example.weather_app

import android.app.Application
import android.content.Context
import com.example.weather_app.Localization.LanguageManager

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        val lang = LanguageManager.getSavedLanguage(base)
        val context = LanguageManager.setLocale(base, lang)
        super.attachBaseContext(context)
    }
}

