package com.example.weather_app.ui.screens.alerts

import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import com.example.weather_app.R
import com.example.weather_app.enums.AlertType
import com.example.weather_app.models.WeatherAlert
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.ui.theme.BabyBlue
import com.example.weather_app.ui.theme.Blue

@Composable
fun AlertCard(alert: WeatherAlert) {
    val icon =
        Icons.Default.Alarm

    val brush = Brush.horizontalGradient(listOf(Blue, BabyBlue))

    Row(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(brush)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Weather Icon",
            modifier = Modifier.size(30.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${alert.startDuration} - ${alert.endDuration}",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular))
            )
        }

        Text(
            text = "Cairo",
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.poppins_light))
        )
    }
}


@Preview
@Composable
private fun AlertCardPreview() {
    AlertCard(WeatherAlert(10, "02:30 AM", "09:45 PM"))
}