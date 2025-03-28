package com.example.weather_app.models

import kotlinx.serialization.Serializable


enum class Screen {
    HOME,
    LOCATION,
    ALERTS,
    SETTINGS,
    SEARCH
}

@Serializable
sealed class NavigationRoutes(val route: String) {

    @Serializable
    data object HomeRoute : NavigationRoutes(Screen.HOME.name)

    @Serializable
    data object LocationsRoute : NavigationRoutes(Screen.LOCATION.name)

    @Serializable
    data object AlertsRoute : NavigationRoutes(Screen.ALERTS.name)

    @Serializable
    data object SettingsRoute : NavigationRoutes(Screen.SETTINGS.name)

    @Serializable
    data object SearchRoute : NavigationRoutes(Screen.SEARCH.name)
}