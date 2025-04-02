package com.example.weather_app.enums

import java.util.Locale

enum class Units(val value: String, val enDegree: String, val arDegree: String) {

    STANDARD("standard", "°K", "°ك"),
    METRIC("metric", "°C", "°س"),
    IMPERIAL("imperial", "°F", "°ف");

    companion object {

        fun getValueByDegree(degree: String): String {
            val language = Locale.getDefault().language

            return if (language == Languages.ARABIC.code) {
                entries.find { it.arDegree == degree }?.value ?: METRIC.value
            } else {
                entries.find { it.enDegree == degree }?.value ?: METRIC.enDegree
            }
        }

        fun getDegreeByValue(value: String): String {
            val language = Locale.getDefault().language

            return if (language == Languages.ARABIC.code) {
                entries.find { it.value == value }?.arDegree ?: METRIC.arDegree
            } else {
                entries.find { it.value == value }?.enDegree ?: METRIC.enDegree
            }

        }
    }
}