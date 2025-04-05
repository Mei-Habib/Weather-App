package com.example.weather_app.utils

import com.example.weather_app.enums.Languages
import com.example.weather_app.enums.Speeds
import com.example.weather_app.enums.Units

object SharedModel {

    var currentDegree = Units.getDegreeByValue(Units.METRIC.value)
    var currentSpeed = Speeds.getDegree(Speeds.METER_PER_SECOND.degree)
    var currentLanguage = Languages.getValueByCode(Languages.ENGLISH.code)
}