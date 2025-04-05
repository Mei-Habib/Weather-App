package com.example.weather_app.enums

import java.util.Locale

enum class Speeds(val degree: String, val arDegree: String) {
    METER_PER_SECOND("m/s", "م/ث"),
    MILE_PER_HOUR("mph", "ميل/س");

    companion object {
        fun getDegree(degree: String): String {
            val language = Locale.getDefault().language

            if (language == Languages.ARABIC.code) {
                return Speeds.values().find { it.degree == degree }?.arDegree
                    ?: METER_PER_SECOND.arDegree
            }
            return degree
        }

        fun getEnglishDegree(degree: String): String {
            val language = Locale.getDefault().language

            if (language == Languages.ARABIC.code) {
                return Speeds.values().find { it.arDegree == degree }?.degree
                    ?: METER_PER_SECOND.degree
            }
            return degree

        }

    }
}