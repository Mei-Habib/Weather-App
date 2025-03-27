package com.example.weather_app.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatUnixTimestamp(unixTime: Int, timezoneOffset: Int): String {
    val instant = Instant.ofEpochSecond((unixTime + timezoneOffset).toLong())
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.of("UTC")) // Adjust manually if needed

    return formatter.format(instant)
}