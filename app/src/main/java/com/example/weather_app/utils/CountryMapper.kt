package com.example.weather_app.utils

import java.util.Locale

object CountryMapper {
    fun getCountryNameFromCode(code: String): String? {
        return try {
            Locale("", code).displayCountry.takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }
}