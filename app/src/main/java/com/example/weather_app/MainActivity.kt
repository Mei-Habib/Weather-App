package com.example.weather_app

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather_app.data.local.WeatherDatabase
import com.example.weather_app.data.local.WeatherLocalDataSource
import com.example.weather_app.data.remote.RetrofitHelper
import com.example.weather_app.data.remote.WeatherRemoteDataSource
import com.example.weather_app.location.LocationManager
import com.example.weather_app.models.NavigationRoutes
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.screens.AlertsScreen
import com.example.weather_app.screens.details.DetailsScreen
import com.example.weather_app.screens.LocationScreen
import com.example.weather_app.screens.SettingsScreen
import com.example.weather_app.screens.details.DetailsFactory
import com.example.weather_app.screens.details.DetailsViewModel
import com.example.weather_app.widgets.BottomNavBar

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var currentLocationState: MutableState<Location>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = LocationManager(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {

            val detailsFactory = DetailsFactory(
                WeatherRepository.getInstance(
                    WeatherRemoteDataSource(
                        RetrofitHelper.apiServices
                    ), WeatherLocalDataSource(WeatherDatabase.getInstance(this).getWeatherDao())
                ), locationManager
            )

            val viewModel =
                ViewModelProvider.create(this, detailsFactory).get(DetailsViewModel::class.java)
            currentLocationState =
                remember { mutableStateOf(Location(android.location.LocationManager.GPS_PROVIDER)) }
            val navController = rememberNavController()
            MainScreen(viewModel, navController)

        }
    }

    override fun onStart() {
        super.onStart()
        checkLocationAccess()
    }

    private fun checkLocationAccess() {
        if (locationManager.hasLocationPermission()) {
            if (locationManager.isLocationEnabled()) {
                fetchUserLocation()
            } else {
                promptEnableLocationDialog()
            }
        } else {
            if (locationManager.shouldShowRationale(this)) {
                showRationaleDialog()
            } else {
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        locationLauncher.launch(
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        )
    }

    private fun fetchUserLocation() {
        locationManager.getCurrentLocation { location ->
            currentLocationState.value = location
            Log.i("TAG", "Location [lon: ${location.longitude}, lat: ${location.latitude}]")
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Permission Required")
            .setMessage("We need your location to provide accurate weather information.")
            .setPositiveButton("OK") { _, _ -> requestLocationPermissions() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun promptEnableLocationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Location Services")
            .setMessage("Please enable location services to access weather data.")
            .setPositiveButton("Enable") { _, _ -> locationManager.promptEnableLocation() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private val locationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isGranted = permissions[ACCESS_FINE_LOCATION] == true ||
                    permissions[ACCESS_COARSE_LOCATION] == true

            if (isGranted) {
                checkLocationAccess() // Check location service if permission is granted
            } else {
                Log.w("Permission", "Location permissions denied.")
            }
        }
}

@Composable
fun MainScreen(viewModel: DetailsViewModel, navController: NavHostController) {
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
                    DetailsScreen(viewModel)
                }

                composable<NavigationRoutes.LocationsRoute> {
                    LocationScreen()
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