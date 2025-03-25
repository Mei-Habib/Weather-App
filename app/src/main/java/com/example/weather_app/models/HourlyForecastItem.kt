package com.example.weather_app.models

import com.example.weather_app.R

data class HourlyForecastItem(val time: String, val icon: Int, val temp: String) {
    companion object {
        val forecast = listOf(
            HourlyForecastItem("2 PM", R.drawable.sunny, "28°"),
            HourlyForecastItem("3 PM", R.drawable.partly_sunny, "27°"),
            HourlyForecastItem("4 PM", R.drawable.cloudy, "26°"),
            HourlyForecastItem("5 PM", R.drawable.light_rainny, "22°"),
            HourlyForecastItem("6 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("7 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("8 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("9 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("10 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("11 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("12 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("1 PM", R.drawable.heavy_rainny, "25°"),
            HourlyForecastItem("2 PM", R.drawable.heavy_rainny, "25°")
        )
    }
}