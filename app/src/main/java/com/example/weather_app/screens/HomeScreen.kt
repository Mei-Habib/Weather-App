package com.example.weather_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.ui.theme.BabyBlue
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.widgets.WeatherTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(drawerState: DrawerState, scope: CoroutineScope, onSearchClick: () -> Unit) {
    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.berlin_germany,
                shouldDisplayMenuIcon = true,
                shouldDisplaySearchIcon = true,
                onSideMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onSearchClick = onSearchClick
            )
        },

        containerColor = Grey
    ) { paddingValues ->
        LocationCard(paddingValues)
    }
}

@Composable
fun LocationCard(paddingValues: PaddingValues) {
    val tempState = remember { mutableIntStateOf(72) }
    val tempUnitState = remember { mutableStateOf("F") }
    val scriptStyleSuper = TextStyle(
        baselineShift = BaselineShift.Superscript,
        fontSize = 14.sp,
        color = Color.White
    )
    val brush = Brush.horizontalGradient(listOf(Blue, BabyBlue))
    Box(
        Modifier
            .padding(top = paddingValues.calculateTopPadding(), start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(brush)
            .fillMaxWidth()
            .height(230.dp)
            .padding(24.dp)

    )
    {
        Column {
            Text(
                text = "Chance of rain 60%",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Partly Cloudy",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 24.sp
                )

                Image(
                    painter = painterResource(R.drawable.partly_cloudy),
                    contentDescription = "partly cloudy icon",
                    Modifier.size(50.dp)
                )
            }

            Spacer(Modifier.height(27.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = stringResource(
                        R.string.location_icon
                    ),
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(7.dp))

                Text(
                    text = "Washington DC, USA", color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${tempState.value}",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontSize = 24.sp
                    )

                    Text(text = "° ${tempUnitState.value}", style = scriptStyleSuper)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_weather_humidity),
                        contentDescription = "humidity percentage",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        "80%",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold))
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_weather_sunny),
                        contentDescription = "UV",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        "0.5",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold))
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_weather_windy),
                        contentDescription = "wind speed",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        "124 mp/h",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold))
                    )
                }
            }


        }

    }
}