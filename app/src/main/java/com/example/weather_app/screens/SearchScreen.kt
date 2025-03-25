package com.example.weather_app.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.weather_app.R
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.widgets.WeatherTopAppBar

@Composable
fun SearchScreen() {
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.search,
                titleContentColor = Dark,
                iconTint = Dark,
            ) {}
        },

        containerColor = Grey
    ) { paddingValues ->
        paddingValues

    }
}