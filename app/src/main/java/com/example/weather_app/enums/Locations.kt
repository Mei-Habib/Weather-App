package com.example.weather_app.enums

import java.util.Locale

enum class Locations(val enValue: String, val arValue: String) {
    GPS("GPS", "نظام تحديد المواقع"),
    MAP("Map", "الخريطة");

    companion object {
        fun getValue(value: String): String {
            val language = Locale.getDefault().language
            return when (language) {
                Languages.ARABIC.code -> {
                    entries.find { it.enValue == value }?.arValue ?: GPS.arValue
                }

                else -> {
                    entries.find { it.enValue == value }?.enValue ?: GPS.enValue
                }

            }
        }
    }
}