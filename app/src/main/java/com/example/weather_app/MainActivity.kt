package com.example.weather_app

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.weather_app.ui.screens.AlertsScreen
import com.example.weather_app.ui.screens.DetailsScreen
import com.example.weather_app.ui.screens.location.LocationScreen
import com.example.weather_app.ui.screens.MapScreen
import com.example.weather_app.ui.screens.SettingsScreen
import com.example.weather_app.utils.ManifestUtils
import com.example.weather_app.viewmodels.DetailsFactory
import com.example.weather_app.viewmodels.DetailsViewModel
import com.example.weather_app.viewmodels.MapViewModel
import com.example.weather_app.components.BottomNavBar
import com.example.weather_app.ui.screens.location.LocationFactory
import com.example.weather_app.ui.screens.location.LocationViewModel
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var currentLocationState: MutableState<Location>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = LocationManager(this)

        val apiKey = ManifestUtils.getApiKeyFromManifest(this)
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }
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

            val locationFactory = LocationFactory(
                WeatherRepository.getInstance(
                    WeatherRemoteDataSource(RetrofitHelper.apiServices),
                    WeatherLocalDataSource(WeatherDatabase.getInstance(this).getWeatherDao())
                )
            )
            val detailsViewModel =
                ViewModelProvider.create(this, detailsFactory).get(DetailsViewModel::class.java)
            val mapViewModel =
                ViewModelProvider.create(this).get(MapViewModel::class.java)
            val locationViewModel =
                ViewModelProvider.create(this, locationFactory).get(LocationViewModel::class.java)
            currentLocationState =
                remember { mutableStateOf(Location(android.location.LocationManager.GPS_PROVIDER)) }
            val navController = rememberNavController()
            MainScreen(detailsViewModel, mapViewModel, locationViewModel, navController)

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    detailsViewModel: DetailsViewModel,
    mapViewModel: MapViewModel,
    locationViewModel: LocationViewModel,
    navController: NavHostController
) {
    val isBottomNavBarVisible = BottomNavBar.mutableNavBarState.observeAsState()
    val currentTitle = remember { mutableStateOf("") }
//
//    // Observe navigation changes and update the title accordingly
//    LaunchedEffect(navController) {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            currentTitle.value = when (destination.route) {
//                NavigationRoutes.HomeRoute.route -> "Home"
//                NavigationRoutes.LocationsRoute -> "Locations"
//                NavigationRoutes.AlertsRoute -> "Alerts"
//                NavigationRoutes.SettingsRoute -> "Settings"
//                else -> "Weather App"
//            }
//        }
//    }

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
                    DetailsScreen(detailsViewModel, currentTitle)
                }

                composable<NavigationRoutes.LocationsRoute> {
                    LocationScreen(locationViewModel) { navController.navigate(NavigationRoutes.SearchRoute) }
                }

                composable<NavigationRoutes.AlertsRoute> {
                    AlertsScreen()
                }

                composable<NavigationRoutes.SettingsRoute> {
                    SettingsScreen()
                }

                composable<NavigationRoutes.SearchRoute> {
                    MapScreen(mapViewModel, detailsViewModel) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}