package com.example.weather_app.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather_app.R
import com.example.weather_app.ui.theme.Blue

@Composable
fun WeatherFloatingActionButton(action: () -> Unit) {
    FloatingActionButton(
        onClick = { action.invoke() },
        backgroundColor = Blue,
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        modifier = Modifier.padding(bottom = 60.dp)

    ) {
        Icon(Icons.Filled.Add, stringResource(R.string.fab), tint = Color.White)
    }
}

@Preview
@Composable
private fun WeatherFloatingActionButtonPreview() {
    WeatherFloatingActionButton {}
}