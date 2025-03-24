package com.example.weather_app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather_app.data.model.NavigationRoutes
import com.example.weather_app.screens.AlertsScreen
import com.example.weather_app.screens.DetailsScreen
import com.example.weather_app.screens.LocationScreen
import com.example.weather_app.screens.SettingsScreen
import com.example.weather_app.widgets.BottomNavBar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            val navController = rememberNavController()
            MainScreen(navController)

        }
    }
}


@Composable
fun MainScreen(navController: NavHostController) {
    val isBottomNavBarVisible = BottomNavBar.mutableNavBarState.observeAsState()

    Scaffold(
        bottomBar = {
            when (isBottomNavBarVisible.value) {
                true -> BottomNavBar.ShowBottomNavBar(navController)
                false -> {}
                null -> {}
            }
        }
    ) { innerPadding ->
        innerPadding
        Box(
            modifier = Modifier
                .background(androidx.compose.ui.graphics.Color.White)
        ) {
            NavHost(navController = navController, startDestination = NavigationRoutes.HomeRoute) {
                composable<NavigationRoutes.HomeRoute> {
                    DetailsScreen()
                }

                composable<NavigationRoutes.LocationsRoute> {
                    LocationScreen(navController)
                }

                composable<NavigationRoutes.AlertsRoute> {
                    AlertsScreen()
                }

                composable<NavigationRoutes.SettingsRoute> {
                    SettingsScreen()
                }
            }
        }
    }
}

