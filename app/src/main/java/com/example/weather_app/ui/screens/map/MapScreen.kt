package com.example.weather_app.ui.screens.map

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weather_app.R
import com.example.weather_app.components.BottomNavBar
import com.example.weather_app.components.BottomSheet
import com.example.weather_app.components.SearchBar
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.ui.screens.details.DetailsViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(mapViewModel: MapViewModel, detailsViewModel: DetailsViewModel, action: () -> Unit) {
    BottomNavBar.mutableNavBarState.value = false
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
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    val coroutineScope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    // insertion
    val insertionState = mapViewModel.insertion.collectAsStateWithLifecycle()

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
                shouldDisplayBack = true,
                iconTint = Dark,
            ) {
                action.invoke()
            }
        },

        containerColor = Grey
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
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

                selectedLocation?.let { latLng ->
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "Selected Location",
                        snippet = "This is the place you selected."
                    )
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                    Log.i("TAG", "MapScreen: LatLng[${latLng.latitude}, ${latLng.longitude}]")
                }
            }

            if (showBottomSheet.value) {
                selectedLocation?.let {
                    BottomSheet(
                        mapViewModel,
                        detailsViewModel,
                        showBottomSheet,
                        it
                    )
                }
            }
        }
    }

    LaunchedEffect(selectedLocation) {
        showBottomSheet.value = selectedLocation != null
    }
}
