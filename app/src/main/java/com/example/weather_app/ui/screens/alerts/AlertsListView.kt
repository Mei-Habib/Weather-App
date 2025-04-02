package com.example.weather_app.ui.screens.alerts

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.ui.screens.locations.SwipeToDeleteContainer

@Composable
fun AlertsListView(
    alerts: List<WeatherAlert>,
    snackbarHostState: SnackbarHostState,
    onDelete: (WeatherAlert) -> Unit
) {
    LazyColumn {
        items(alerts, key = { it.id }) {
            SwipeToDeleteContainer(
                item = it,
                onDelete = { onDelete(it) },
                snackbarHostState = snackbarHostState,

                ) {
                AlertCard(it)
            }
        }
    }

    Spacer(modifier = Modifier.height(150.dp))
}
