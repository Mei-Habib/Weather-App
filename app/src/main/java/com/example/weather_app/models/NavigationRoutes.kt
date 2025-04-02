package com.example.weather_app.models

import kotlinx.serialization.Serializable


enum class Screen {
    HOME,
    DETAILS,
    LOCATION,
    ALERTS,
    SETTINGS,
    MAP
}

@Serializable
sealed class NavigationRoutes(val route: String) {

    @Serializable
    data object HomeRoute : NavigationRoutes(Screen.HOME.name)

    @Serializable
    data object DetailsRoute : NavigationRoutes(Screen.DETAILS.name)

    @Serializable
    data object LocationsRoute : NavigationRoutes(Screen.LOCATION.name)

    @Serializable
    data object AlertsRoute : NavigationRoutes(Screen.ALERTS.name)

    @Serializable
    data object SettingsRoute : NavigationRoutes(Screen.SETTINGS.name)

    @Serializable
    data object MapRoute : NavigationRoutes(Screen.MAP.name)
}