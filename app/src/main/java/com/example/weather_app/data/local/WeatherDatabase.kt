package com.example.weather_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather_app.models.FavoriteLocation

@Database(entities = [FavoriteLocation::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao

    companion object {
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_database")
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}