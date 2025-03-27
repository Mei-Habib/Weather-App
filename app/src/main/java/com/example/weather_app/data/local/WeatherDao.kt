package com.example.weather_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.weather_app.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteLocation(favouriteLocation: FavoriteLocation): Long

    @Delete
    fun deleteFavoriteLocation(favouriteLocation: FavoriteLocation): Int
}