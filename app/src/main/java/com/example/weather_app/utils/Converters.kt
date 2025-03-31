package com.example.weather_app.utils

import androidx.room.TypeConverter
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherDetails
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.common.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        return latLng?.let {
            "${it.latitude}, ${it.longitude}"
        }
    }

    @TypeConverter
    fun toLatLng(value: String?): LatLng? {
        return value?.split(",")?.let {
            LatLng(it[0].toDouble(), it[1].toDouble())
        }
    }

    @TypeConverter
    fun fromWeather(weather: WeatherDetails?): String? {
        return weather?.let {
            gson.toJson(weather)
        }
    }

    @TypeConverter
    fun toWeather(value: String?): WeatherDetails? {
        return value?.let {
            gson.fromJson(it, object : TypeToken<WeatherDetails>() {}.type)
        }
    }

    @TypeConverter
    fun fromJson(json: String): FavoriteLocation {
        return gson
            .fromJson(json, FavoriteLocation::class.java)
    }

    @TypeConverter
    fun toJson(favLocation: FavoriteLocation): String {
        return gson.toJson(favLocation)
    }
}