package com.example.weather_app.ui.screens.settings

import android.app.Activity
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weather_app.Localization.LanguageManager
import com.example.weather_app.R
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.enums.Languages
import com.example.weather_app.enums.Locations
import com.example.weather_app.enums.Speeds
import com.example.weather_app.enums.Units
import com.example.weather_app.utils.changeLanguage


@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val context = LocalContext.current
    val selectedTemp by viewModel.temp.collectAsStateWithLifecycle()
    val selectedWindSpeed by viewModel.windSpeed.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.language.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.location.collectAsStateWithLifecycle()

    val tempOptions = listOf(
        Units.getDegreeByValue(Units.METRIC.value),
        Units.getDegreeByValue(Units.STANDARD.value),
        Units.getDegreeByValue(Units.IMPERIAL.value)
    )
    Log.i("TAG", "SettingsScreen: ${Units.getDegreeByValue(Units.METRIC.value)}")
    Log.i("TAG", "SettingsScreen: ${Speeds.getDegree(Speeds.METER_PER_SECOND.degree)}")

    val windOptions = listOf(
        Speeds.getDegree(Speeds.METER_PER_SECOND.degree),
        Speeds.getDegree(Speeds.MILE_PER_HOUR.degree)
    )
    val locOptions =
        listOf(Locations.getValue(Locations.GPS.enValue), Locations.getValue(Locations.MAP.enValue))
    val langOptions = listOf(Languages.ENGLISH.value, Languages.ARABIC.value)

    LaunchedEffect(Unit) {
        viewModel.refreshValues()
    }
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.settings,
                titleContentColor = Dark,
                iconTint = Dark,
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

                    MultiStateToggleButton(options = tempOptions, selectedOption = selectedTemp) {
                        viewModel.updateTemp(it)
                        Log.i("TAG", "SettingsScreen: ${viewModel.temp.value}")
                    }
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

                    MultiStateToggleButton(options = windOptions, selectedWindSpeed) {
                        viewModel.updateWindSpeed(it)
                        Log.i("TAG", "SettingsScreen: ${viewModel.windSpeed.value}")
                    }
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

                    MultiStateToggleButton(options = locOptions, selectedLocation) { }
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

                    MultiStateToggleButton(options = langOptions, selectedLanguage) {
                        viewModel.updateLanguage(it)
                        changeLanguage(context, it)
                        (context as Activity).recreate()
                        Log.i("TAG", "SettingsScreen: lang ${viewModel.language.value}")
                    }
                }

            }

        }
    }
}

@Composable
fun MultiStateToggleButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Grey)
    ) {
        options.forEachIndexed { _, option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .width(55.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Blue else Grey)
                    .clickable {
                        onOptionSelected(option)
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color.White else Dark
                )
            }
        }
    }
}


