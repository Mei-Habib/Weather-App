package com.example.weather_app.ui.screens.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weather_app.R
import com.example.weather_app.components.BottomNavBar
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.components.WeatherFloatingActionButton
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.DarkGrey


@Composable
fun LocationScreen(viewModel: LocationViewModel, action: () -> Unit) {
    BottomNavBar.mutableNavBarState.value = true
    val locations = remember {
        mutableStateListOf(
            "Liverpool, United Kingdom",
            "Alexandria, Egypt",
            "Tokyo, Japan"
        )
    }
    val context = LocalContext.current
    val favoriteLocation = viewModel.favoriteLocations.collectAsStateWithLifecycle()
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
            WeatherFloatingActionButton(action)
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
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .background(
                    Grey
                )
        ) {
            items(items = locations, key = { it }) { loc ->
                SwipeToDeleteContainer(
                    item = loc,
                    onDelete = {
                        locations -= loc
                    },
                    snackbarHostState = snackbarHostState
                ) { location ->
                    LocationCard(location, 32, "Partly Cloudy") {}
                }
            }
        }

    }
}
