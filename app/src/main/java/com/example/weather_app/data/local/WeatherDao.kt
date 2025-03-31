package com.example.weather_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather_app.models.FavoriteLocation

import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long

    @Delete
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int
}