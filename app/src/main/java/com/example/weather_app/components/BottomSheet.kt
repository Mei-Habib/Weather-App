package com.example.weather_app.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weather_app.R
import com.example.weather_app.data.remote.Response
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherDetails
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.DarkGrey
import com.example.weather_app.utils.CountryMapper
import com.example.weather_app.utils.IconsMapper
import com.example.weather_app.ui.screens.details.DetailsViewModel
import com.example.weather_app.ui.screens.map.MapViewModel
import com.google.android.gms.maps.model.LatLng

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    mapViewModel: MapViewModel,
    detailsViewModel: DetailsViewModel,
    showBottomSheet: MutableState<Boolean>,
    location: LatLng
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    detailsViewModel.getWeatherDetails(location.latitude, location.longitude)
    val weatherState = detailsViewModel.weatherData.observeAsState()
    val saveState = remember { mutableStateOf(false) }


    when (weatherState.value) {
        is Response.Loading -> {}

        is Response.Failure -> {}
        is Response.Success -> {
            val weather = (weatherState.value as Response.Success<WeatherDetails>).response
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false

                },
                sheetState = sheetState,
                tonalElevation = 16.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                ) {

                    if (saveState.value) {
                        Icon(
                            painter = painterResource(R.drawable.ic_saved),
                            contentDescription = stringResource(
                                R.string.save_icon
                            ),
                            tint = Dark,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(30.dp)
                                .align(Alignment.End)
                                .clickable {
                                    // delete
                                }
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_save),
                            contentDescription = stringResource(
                                R.string.save_icon
                            ),
                            tint = Dark,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(30.dp)
                                .align(Alignment.End)
                                .clickable {
                                    saveState.value = true
                                    mapViewModel.insertFavoriteLocation(
                                        FavoriteLocation(
                                            weather.weather.name,
                                            location,
                                            weather.weather.sys.country,
                                            weather
                                        )
                                    )

                                    Log.i("TAG", "Place Saved Successfully")
                                },
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.ic_location),
                                tint = Dark,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(24.dp),
                                contentDescription = stringResource(R.string.location_icon)
                            )
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "${CountryMapper.getCountryNameFromCode(weather.weather.sys.country)}",
                                    fontSize = 16.sp,
                                    color = Dark,
                                    fontFamily = FontFamily(Font(R.font.poppins_semi_bold))
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = weather.weather.name,
                                    fontSize = 14.sp,
                                    color = DarkGrey,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Image(
                                painter = painterResource(
                                    IconsMapper.iconsMap.get(weather.weather.weather.firstOrNull()?.icon)
                                        ?: R.drawable.partly_sunny
                                ),
                                contentDescription = "Sunny",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(34.dp)
                            )

                            Text(
                                text = "${weather.weather.main.temp}°",
                                fontSize = 19.sp,
                                color = DarkGrey,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )

                        }
                    }

                    Divider(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        color = DarkGrey,
                        thickness = .5.dp
                    )

                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "Longitude and latitude",
                                fontSize = 14.sp,
                                color = DarkGrey,
                                fontFamily = FontFamily(Font((R.font.poppins_regular)))
                            )

                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = String.format(
                                    "%.7f, %.5f",
                                    location.longitude,
                                    location.latitude
                                ),
                                fontSize = 14.sp,
                                color = Dark,
                                fontFamily = FontFamily(Font((R.font.poppins_medium)))
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Wind",
                                fontSize = 14.sp,
                                color = DarkGrey,
                                fontFamily = FontFamily(Font((R.font.poppins_regular)))
                            )

                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${weather.weather.wind.speed}",
                                fontSize = 14.sp,
                                color = Dark,
                                fontFamily = FontFamily(Font((R.font.poppins_medium)))
                            )
                        }
                    }
                }
            }


        }

        else -> {}
    }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet.value) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

}





