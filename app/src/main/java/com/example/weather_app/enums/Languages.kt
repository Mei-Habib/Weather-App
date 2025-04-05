package com.example.weather_app.enums

enum class Languages(val value: String, val code: String) {

    ENGLISH("English", "en"),
    ARABIC("العربية", "ar");


    companion object {
        fun getCodeByValue(value: String): String {
            return entries.find { it.value == value }?.code ?: ENGLISH.code
        }

        fun getValueByCode(code: String): String {
            return entries.find { it.code == code }?.value ?: ENGLISH.value
        }
    }
}
