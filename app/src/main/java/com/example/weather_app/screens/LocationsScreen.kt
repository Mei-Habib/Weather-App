package com.example.weather_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weather_app.R
import com.example.weather_app.ui.theme.BabyBlue
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.widgets.WeatherFloatingActionButton
import com.example.weather_app.widgets.WeatherTopAppBar

@Composable
fun LocationScreen(navController: NavHostController) {
    val locations = listOf("Liverpool, United Kingdom", "Alexandria, Egypt", "Tokyo, Japan")
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.saved_locations,
                titleContentColor = Dark,
                iconTint = Dark,
            ) {
                navController.popBackStack()
            }
        },

        floatingActionButton = {
            WeatherFloatingActionButton {}
        },

        containerColor = Grey
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .background(
                    Grey
                )
        ) {
            items(locations) { loc ->
                LocationCard(loc, 32, "Partly Cloudy") {}
            }
        }

    }
}

@Composable
fun LocationCard(location: String, temperature: Int, condition: String, action: () -> Unit) {
    val brush = Brush.horizontalGradient(listOf(Blue, BabyBlue))

    Row(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(brush)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable { action.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.partly_cloudy),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(45.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = location,
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semi_bold))
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = condition,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular))
            )
        }

        Text(
            text = "$temperature°",
            color = Color.White,
            fontSize = 36.sp,
            fontFamily = FontFamily(Font(R.font.poppins_light))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocationCardPreview() {
    LocationCard(
        location = "Liverpool, United Kingdom",
        temperature = 32,
        condition = "Partly Cloudy"
    ) {}
}