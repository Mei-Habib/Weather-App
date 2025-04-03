package com.example.weather_app.ui.screens.locations

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weather_app.R
import com.example.weather_app.components.BottomNavBar
import com.example.weather_app.components.CustomProgressIndicator
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.components.WeatherFloatingActionButton
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.data.remote.Response
import com.example.weather_app.models.FavoriteLocation.Companion.toJson


@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
    onSavedLocationCLick: (String) -> Unit,
    onAddLocationCLick: () -> Unit
) {
    BottomNavBar.mutableNavBarState.value = true
    val context = LocalContext.current
    val favoriteLocations = viewModel.favoriteLocations.collectAsStateWithLifecycle()
    val message = viewModel.message
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getFavoriteLocations()
    }

    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.saved_locations,
                titleContentColor = Dark,
                iconTint = Dark,
            ) {

            }
        },

        floatingActionButton = {
            WeatherFloatingActionButton(onAddLocationCLick)
        },

        containerColor = Grey,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                Snackbar(
                    it,
                    containerColor = Color(0xFF2E3135),
                    actionColor = Color(0xFF73B4FF),
                )
            }
        }

    ) { paddingValues ->

        when (favoriteLocations.value) {
            is Response.Loading -> CustomProgressIndicator()
            is Response.Failure -> {}
            is Response.Success -> {
                val savedLocations = (favoriteLocations.value as Response.Success).response
                var list by remember { mutableStateOf(savedLocations) }

                if (list.isNullOrEmpty()) {
                    EmptyLocationsView()
                }

                when (favoriteLocations.value) {
                    is Response.Loading -> {}
                    is Response.Failure -> EmptyLocationsView()
                    is Response.Success -> {
                        SavedLocationsListView(paddingValues, snackbarHostState, list, onDelete = {
                            viewModel.deleteFavoriteLocation(it)
                            list -= it
                        }) {
                            val gson = it.toJson()
                            onSavedLocationCLick(gson)
                        }
                    }
                }

            }

        }


    }
}
