package com.example.weather_app.ui.screens.locations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.ui.theme.Grey

@Composable
fun SavedLocationsListView(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    savedLocations: List<FavoriteLocation>,
    onDelete: (FavoriteLocation) -> Unit,
    onSelected: (FavoriteLocation) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .background(
                Grey
            )
    ) {
        items(items = savedLocations, key = { it.city }) { loc ->
            SwipeToDeleteContainer(
                item = loc,
                onDelete = { onDelete(it) },
                snackbarHostState = snackbarHostState
            ) { location ->
                FavoriteLocationCard(
                    location
                ) {
                    onSelected(location)
                }
            }
        }
    }
}