package com.example.weather_app.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes {

    @Serializable
    data object HomeRoute : NavigationRoutes()

    @Serializable
    data object LocationsRoute : NavigationRoutes()

    @Serializable
    data object AlertsRoute : NavigationRoutes()

    @Serializable
    data object SettingsRoute : NavigationRoutes()

}