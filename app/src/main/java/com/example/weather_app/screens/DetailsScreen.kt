package com.example.weather_app.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.weather_app.viewmodels.DetailsViewModel
import com.example.weather_app.widgets.WeatherTopAppBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsScreen(viewModel: DetailsViewModel, currentTitle: MutableState<String>) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherContent(weatherDetailsState: Response<WeatherDetails>?, paddingValues: PaddingValues) {

    when (weatherDetailsState) {
        is Response.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
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


            Box(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                                text = "${weatherDetails.weather.main.temp.toInt()}°",
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
//                        horizontalArrangement = Arrangement.SpaceBetween,
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
            text = "${forecastItem.main.temp.toInt()}",
            color = Dark,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontSize = 16.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyWeatherForecast(dailyList: List<ForecastResponse.Item>) {
    val context = LocalContext.current
    val date = getDayFormTimestamp(dailyList.get(0).dt, context)
    val icon = dailyList.first().weather.get(0).icon

    var min = dailyList.get(0).main.temp_min.toInt()
    var max = dailyList.get(0).main.temp_max.toInt()


    for (item in dailyList) {
        min = Math.min(item.main.temp_min.toInt(), min)
        max = Math.max(item.main.temp_max.toInt(), max)
    }
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(
                    IconsMapper.iconsMap.get(icon)
                        ?: R.drawable.clear_sky_day
                ),
                contentDescription = "condition icon",
                modifier = Modifier
                    .padding(end = 17.dp)
                    .size(30.dp)

            )

            Text(
                text = date,
                color = Dark,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 14.sp
            )
        }

        Text(
            text = "$max°/ $min°",
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontSize = 14.sp
        )

    }
}

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
                value = "${weatherDetails.weather.main.feels_like.toInt()}°",
                unit = stringResource(R.string.feels_like)
            )
            WeatherCard(
                icon = R.drawable.ic_wind,
                value = "${weatherDetails.weather.wind.speed} mp/h",
                unit = stringResource(R.string.wind_speed)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WeatherCard(icon = R.drawable.ic_sun, value = "9", unit = "UV Index")
            WeatherCard(
                icon = R.drawable.ic_humidity,
                value = "${weatherDetails.weather.main.humidity}%",
                unit = stringResource(
                    R.string.humidity
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WeatherCard(
                icon = R.drawable.ic_sun,
                value = "${weatherDetails.weather.clouds.all}",
                unit = "Clouds"
            )
            WeatherCard(
                icon = R.drawable.ic_humidity,
                value = "${weatherDetails.weather.main.humidity}%",
                unit = stringResource(
                    R.string.humidity
                )
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
