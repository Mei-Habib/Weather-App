package com.example.weather_app.data.model

import com.example.weather_app.R

data class DailyForecastItem(
    val day: String,
    val highTemp: String,
    val lowTemp: String,
    val icon: Int
) {
    companion object {
        val weatherData = listOf(
            DailyForecastItem("Yesterday", "20", "8", R.drawable.sunny),
            DailyForecastItem("Today", "24", "10", R.drawable.partly_sunny),
            DailyForecastItem("Tomorrow", "28", "13", R.drawable.cloudy),
            DailyForecastItem("Tuesday", "32", "14", R.drawable.light_rainny),
            DailyForecastItem("Wednesday", "35", "17", R.drawable.heavy_rainny),
            DailyForecastItem("Thursday", "37", "21", R.drawable.heavy_rainny),
            DailyForecastItem("Friday", "34", "16", R.drawable.cloudy)
        )
    }
}