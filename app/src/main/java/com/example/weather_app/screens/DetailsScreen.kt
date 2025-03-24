package com.example.weather_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.data.model.DailyForecastItem
import com.example.weather_app.data.model.HourlyForecastItem
import com.example.weather_app.data.model.WeatherDetails
import com.example.weather_app.ui.theme.BabyBlue
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.DarkGrey
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.widgets.WeatherTopAppBar

@Composable
fun DetailsScreen() {
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.berlin_germany,
                titleContentColor = Color.White,
                iconTint = Color.White,
                onBackClick = {
                    // pop
                }
            )
        },

        containerColor = Grey
    ) { paddingValues ->
        val brush = Brush.verticalGradient(listOf(Blue, BabyBlue))
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
                    Image(
                        painter = painterResource(R.drawable.partly_cloudy),
                        contentDescription = stringResource(R.string.weather_condition),
                        Modifier
                            .padding(top = paddingValues.calculateTopPadding())
                            .size(80.dp)
                    )

                    Text(
                        text = "Partly Cloudy",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 24.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Tuesday, 24 August 2020",
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(HourlyForecastItem.forecast.size) {
                        HourlyWeatherForecast(HourlyForecastItem.forecast[it])
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
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(DailyForecastItem.weatherData.size) {
                        DailyWeatherForecast(DailyForecastItem.weatherData[it])
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

            WeatherDashboard()
        }
    }
}

@Composable
fun HourlyWeatherForecast(forecastItem: HourlyForecastItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 24.dp)
    ) {
        Text(
            text = forecastItem.time,
            color = DarkGrey,
            fontFamily = FontFamily(Font(R.font.poppins_light)),
            fontSize = 12.sp
        )

        Image(
            painter = painterResource(forecastItem.icon),
            contentDescription = "weather icon",
            modifier = Modifier
                .padding(8.dp)
                .size(28.dp)
        )

        Text(
            text = forecastItem.temp,
            color = Dark,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontSize = 14.sp
        )
    }
}

@Composable
fun DailyWeatherForecast(dailyForecastItem: DailyForecastItem) {
    Row(
        modifier = Modifier
            .padding(bottom = 25.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(dailyForecastItem.icon),
                contentDescription = "condition icon",
                modifier = Modifier
                    .padding(end = 17.dp)
                    .size(30.dp)

            )

            Text(
                text = dailyForecastItem.day,
                color = Dark,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 14.sp
            )
        }

        Text(
            text = "${dailyForecastItem.highTemp}°/ ${dailyForecastItem.lowTemp}°",
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            fontSize = 14.sp
        )

    }
}

@Composable
fun WeatherDashboard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 890.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            WeatherCard(drawable = R.drawable.ic_temp, value = "72°", unit = "Fahrenheit")
            WeatherCard(drawable = R.drawable.ic_windy, value = "0.2", unit = "UV Index")
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            WeatherCard(drawable = R.drawable.ic_sunny, value = "134 mp/h", unit = "Pressure")
            WeatherCard(drawable = R.drawable.ic_humidity, value = "48%", unit = "Humidity")
        }
    }
}


@Composable
fun WeatherCard(drawable: Int, value: String, unit: String) {
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
                painter = painterResource(drawable),
                contentDescription = "feels like",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "$value°",
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontSize = 16.sp,
                    color = Dark
                )
                Text(
                    text = unit,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 12.sp,
                    color = Grey
                )
            }

        }
    }

}
