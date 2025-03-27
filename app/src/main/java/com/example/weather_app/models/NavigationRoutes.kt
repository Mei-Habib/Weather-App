package com.example.weather_app.models

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(val route : String) {

    @Serializable
    data object HomeRoute : NavigationRoutes("home")

    @Serializable
    data object LocationsRoute : NavigationRoutes("location")

    @Serializable
    data object AlertsRoute : NavigationRoutes("alerts")

    @Serializable
    data object SettingsRoute : NavigationRoutes("settings")

}