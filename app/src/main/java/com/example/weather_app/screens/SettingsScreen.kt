package com.example.weather_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.widgets.WeatherTopAppBar

@Composable
fun SettingsScreen() {
    val tempOptions = listOf("C", "K", "F")
    val windOptions = listOf("m/s", "mil/h")
    val locOptions = listOf("Map", "GPS")
    val langOptions = listOf("Ar", "En", "Sys")

    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.settings,
                titleContentColor = Dark,
                iconTint = Dark,
                onBackClick = {
                    // pop
                }
            )
        },

        containerColor = Grey
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .background(Grey)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(300.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.weather),
                        color = Dark,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )

                    MultiStateToggleButton(tempOptions) { }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.wind_speed),
                        color = Dark,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )

                    MultiStateToggleButton(windOptions) { }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.location),
                        color = Dark,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )

                    MultiStateToggleButton(locOptions) { }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.language),
                        color = Dark,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )

                    MultiStateToggleButton(langOptions) { }
                }

            }

        }
    }
}

@Composable
fun MultiStateToggleButton(
    options: List<String>,
    selectedIndex: Int = 0,
    onStateChange: (Int) -> Unit
) {
    var currentIndex by remember { mutableStateOf(selectedIndex) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Grey)
    ) {
        options.forEachIndexed { index, option ->
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (index == currentIndex) Blue else Grey)
                    .clickable {
                        currentIndex = index
                        onStateChange(index)
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (index == currentIndex) Color.White else Dark
                )
            }
        }
    }
}

@Preview
@Composable
fun MultiStateToggleButtonPreview() {
    MultiStateToggleButton(
        options = listOf("°C", "°K", "°F"),
        onStateChange = { index -> println("Selected: $index") }
    )
}

