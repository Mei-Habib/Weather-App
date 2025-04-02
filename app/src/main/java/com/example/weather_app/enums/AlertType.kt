package com.example.weather_app.enums

import java.util.Locale

enum class AlertType(val enValue: String, val arValue: String) {
    ALARM("Alarm", "المنبه"),
    NOTIFICATION("Notification", "الاشعار");

    fun getLocalizedValue(): String {
        val lang = Locale.getDefault().language
        return when (lang) {
            "ar" -> arValue
            else -> enValue
        }
    }

    companion object {
        fun getAbsoluteValue(value: String): AlertType {
            val lang: String = Locale.getDefault().language
            return when (lang) {
                "ar" -> entries.find { it.arValue == value } ?: ALARM
                else -> entries.find { it.enValue == value } ?: ALARM
            }
        }
    }
}