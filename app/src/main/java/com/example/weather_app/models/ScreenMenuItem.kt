package com.example.weather_app.models

import androidx.annotation.DrawableRes
import com.example.weather_app.R

data class ScreenMenuItem(
    val route: NavigationRoutes,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val id: Int
) {
    companion object {
        val menuItems = listOf(
            ScreenMenuItem(NavigationRoutes.HomeRoute, R.drawable.ic_home, R.drawable.ic_baseline_home_avd, 1),
            ScreenMenuItem(
                NavigationRoutes.LocationsRoute,
                R.drawable.ic_add_location,
                R.drawable.ic_baseline_loc_avd,
                2
            ),
            ScreenMenuItem(
                NavigationRoutes.AlertsRoute,
                R.drawable.ic_add_alert,
                R.drawable.ic_baseline_alert_avd,
                3
            ),
            ScreenMenuItem(
                NavigationRoutes.SettingsRoute,
                R.drawable.ic_settings,
                R.drawable.ic_baseline_settings_avd,
                4
            ),
        )
    }
}
