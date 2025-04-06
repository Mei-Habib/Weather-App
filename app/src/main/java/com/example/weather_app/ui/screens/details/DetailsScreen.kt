package com.example.weather_app.ui.screens.details

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.components.BottomNavBar
import com.example.weather_app.components.CustomProgressIndicator
import com.example.weather_app.data.remote.Response
import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.models.WeatherDetails
import com.example.weather_app.ui.theme.BabyBlue
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.DarkGrey
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.utils.IconsMapper
import com.example.weather_app.utils.getDayFormTimestamp
import com.example.weather_app.utils.getDayFromTimestamp
import com.example.weather_app.utils.getDaysForecast
import com.example.weather_app.utils.getHourFormTime
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.ui.screens.settings.SettingsViewModel
import com.example.weather_app.utils.SharedModel
import com.example.weather_app.utils.convertSpeed
import com.example.weather_app.utils.convertTemperature

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: DetailsViewModel,
    currentTitle: MutableState<String>,
) {
    BottomNavBar.mutableNavBarState.value = true
    val locationState = viewModel.location.observeAsState()
    val weatherDetailsState = viewModel.weatherData.observeAsState()

    LaunchedEffect(weatherDetailsState.value) {
        if (weatherDetailsState.value is Response.Success) {
            val weather = (weatherDetailsState.value as Response.Success).response
            currentTitle.value = weather.weather.name
        }
    }

    LaunchedEffect(locationState) {
        locationState.value?.let { loc ->
            viewModel.getWeatherDetails(loc.latitude, loc.longitude)
        }
    }
    Log.i("TAG", "DetailsScreen: ${weatherDetailsState.value}")
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleString = currentTitle.value,
                titleContentColor = Color.White,
                iconTint = Color.White,
                onBackClick = {
                    // pop
                }
            )
        },

        containerColor = Grey
    ) { paddingValues ->
        WeatherContent(weatherDetailsState.value, paddingValues)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherContent(
    weatherDetailsState: Response<WeatherDetails>?,
    paddingValues: PaddingValues
) {

    when (weatherDetailsState) {
        is Response.Loading -> {
            CustomProgressIndicator()
        }

        is Response.Failure -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${weatherDetailsState.err.message}",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        }

        is Response.Success -> {
            val weatherDetails = weatherDetailsState.response
            val brush = Brush.verticalGradient(listOf(Blue, BabyBlue))
            val timestamp = weatherDetails.weather.dt
            val timezoneOffset = weatherDetails.weather.timezone
            val formattedDate = getDayFromTimestamp(timestamp, timezoneOffset)
//            Log.i("TAG", "WeatherContent: ${settingsViewModel.temp.value}")
            Log.i("TAG", "WeatherContent: shared model temp unit  ${SharedModel.currentDegree}")
            Log.i("TAG", "WeatherContent: shared model speed unit ${SharedModel.currentSpeed}")
            Log.i("TAG", "WeatherContent: shared model lang ${SharedModel.currentLanguage}")


            Box(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(brush)
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Row(
                            Modifier
                                .padding(top = paddingValues.calculateTopPadding()),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${
                                    convertTemperature(
                                        weatherDetails.weather.main.feels_like.toInt(),
                                        SharedModel.currentDegree
                                    )
                                } ${SharedModel.currentDegree}",
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                fontSize = 36.sp
                            )

                            Image(
                                painter = painterResource(
                                    IconsMapper.iconsMap.get(weatherDetails.weather.weather.firstOrNull()?.icon)
                                        ?: R.drawable.clear_sky_day
                                ),
                                contentDescription = stringResource(R.string.weather_icon),
                                modifier = Modifier.size(60.dp)
                            )

                        }

                        Text(
                            text = "${weatherDetails.weather.weather.firstOrNull()?.description}",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontSize = 24.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "$formattedDate",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            fontSize = 16.sp
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 270.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .blur(20.dp)
                        .background(Color.White.copy(alpha = 0.9f))

                ) {

                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val map = weatherDetails.forecast.getDaysForecast()
                        val firstDayForecastList = map.values.firstOrNull() ?: emptyList()
                        items(firstDayForecastList) { forecastItem ->
                            HourlyWeatherForecast(forecastItem)
                        }

                    }
                }

                Text(
                    text = stringResource(R.string.forecast),
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontSize = 16.sp,
                    color = Dark,
                    modifier = Modifier.padding(top = 450.dp, start = 24.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 480.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)

                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        val map = weatherDetails.forecast.getDaysForecast()
                        items(map.entries.toList()) { (_, list) ->
                            DailyWeatherForecast(list)
                        }
                    }
                }

                Text(
                    text = "Details",
                    color = Dark,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    modifier = Modifier.padding(top = 860.dp, start = 20.dp)
                )

                WeatherDashboard(weatherDetails)
            }
        }

        else -> {}
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyWeatherForecast(forecastItem: ForecastResponse.Item) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Text(
            text = getHourFormTime(forecastItem.dt.toLong()),
            color = DarkGrey,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            fontSize = 14.sp
        )

        Image(
            painter = painterResource(
                IconsMapper.iconsMap.get(forecastItem.weather.get(0).icon)
                    ?: R.drawable.clear_sky_day
            ),
            contentDescription = "Sunset",
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = "${
                convertTemperature(
                    forecastItem.main.temp.toInt(),
                    SharedModel.currentDegree
                )
            } ${SharedModel.currentDegree}",
            color = Dark,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontSize = 14.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyWeatherForecast(dailyList: List<ForecastResponse.Item>) {
    val context = LocalContext.current
    val icon = dailyList.first().weather[0].icon
    val date = getDayFormTimestamp(dailyList[0].dt, context)

    var min = convertTemperature(dailyList[0].main.temp_min.toInt(), SharedModel.currentDegree)
    var max = convertTemperature(dailyList[0].main.temp_max.toInt(), SharedModel.currentDegree)

    for (item in dailyList) {
        min = minOf(min, convertTemperature(item.main.temp_min.toInt(), SharedModel.currentDegree))
        max = maxOf(max, convertTemperature(item.main.temp_max.toInt(), SharedModel.currentDegree))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = date,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.poppins_medium)),
            color = Dark,
            modifier = Modifier.width(90.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(IconsMapper.iconsMap[icon] ?: R.drawable.clear_sky_day),
                contentDescription = "weather icon",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "$min ${SharedModel.currentDegree} / $max ${SharedModel.currentDegree}",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            color = Dark,
            maxLines = 1
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition", "DefaultLocale")
@Composable
fun WeatherDashboard(weatherDetails: WeatherDetails) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 880.dp, start = 16.dp, end = 16.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WeatherCard(
                icon = R.drawable.ic_thermometer,
                value = "${
                    convertTemperature(
                        weatherDetails.weather.main.temp.toInt(),
                        SharedModel.currentDegree
                    )
                } ${SharedModel.currentDegree}",
                unit = stringResource(R.string.feels_like)
            )
            WeatherCard(
                icon = R.drawable.ic_wind,
                value = "${
                    String.format(
                        "%.1f",
                        convertSpeed(weatherDetails.weather.wind.speed, SharedModel.currentSpeed)
                    )
                } ${SharedModel.currentSpeed}",
                unit = stringResource(R.string.wind_speed)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WeatherCard(
                icon = R.drawable.ic_pressure,
                value = "${weatherDetails.weather.main.pressure}",
                unit = stringResource(
                    R.string.pressure
                )
            )
            WeatherCard(
                icon = R.drawable.ic_humidity,
                value = "${weatherDetails.weather.main.humidity}%",
                unit = stringResource(
                    R.string.humidity
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            WeatherCard(
                icon = R.drawable.ic_cloud,
                value = "${weatherDetails.weather.clouds.all}",
                unit = "Clouds"
            )
        }
    }
}


@Composable
fun WeatherCard(icon: Int, value: String, unit: String) {
    Card(
        modifier = Modifier
            .size(width = 160.dp, height = 100.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "feels like",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = unit,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 12.sp,
                    color = Color.Gray,
                )

                Text(
                    text = "$value",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontSize = 16.sp,
                    color = Dark,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

        }
    }

}
