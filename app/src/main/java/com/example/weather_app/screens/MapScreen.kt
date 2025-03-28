package com.example.weather_app.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.weather_app.R
import com.example.weather_app.components.SearchBar
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.viewmodels.MapViewModel
import com.example.weather_app.widgets.WeatherTopAppBar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(mapViewModel: MapViewModel, action:()->Unit) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val userLocation by mapViewModel.userLocation
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mapViewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            Log.e("TAG", "Location permission was denied by the user.")
        }
    }
    val selectedLocation by mapViewModel.selectedLocation

// Request the location permission when the composable is launched
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            // Check if the location permission is already granted
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Fetch the user's location and update the camera
                mapViewModel.fetchUserLocation(context, fusedLocationClient)
            }

            else -> {
                // Request the location permission if it has not been granted
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.search,
                titleContentColor = Dark,
                iconTint = Dark,
            ) {
                action.invoke()
            }
        },

        containerColor = Grey
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding(), bottom = 40.dp)
                .fillMaxSize()
        ) {
            SearchBar { place ->
                mapViewModel.selectLocation(place, context)
            }

            GoogleMap(
                cameraPositionState = cameraPositionState
            ) {
                userLocation?.let { latLng ->
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "",
                        snippet = stringResource(R.string.this_is_where_you_are_currently_located)
                    )
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                }

                selectedLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Selected Location",
                        snippet = "This is the place you selected."
                    )
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                }

            }
        }
    }


}