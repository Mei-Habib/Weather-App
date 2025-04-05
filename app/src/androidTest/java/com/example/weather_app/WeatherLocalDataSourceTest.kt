package com.example.weather_app

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather_app.data.local.IWeatherLocalDataSource
import com.example.weather_app.data.local.WeatherLocalDataSource
import com.example.weather_app.data.local.room.WeatherDao
import com.example.weather_app.data.local.room.WeatherDatabase
import com.example.weather_app.data.local.sharedpreferences.ISettingsSharedPreferences
import com.example.weather_app.data.local.sharedpreferences.SettingsSharedPreferences
import com.example.weather_app.models.WeatherAlert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao
    private lateinit var sharedPreferences: ISettingsSharedPreferences
    private lateinit var localDataSource: IWeatherLocalDataSource
    private lateinit var context: Context

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = database.getWeatherDao()
        context = ApplicationProvider.getApplicationContext<Application>()
        sharedPreferences = SettingsSharedPreferences.getInstance(context)
        localDataSource = WeatherLocalDataSource(dao, sharedPreferences)
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun insertAlert_retrieveAlert() = runTest {
        val alert = WeatherAlert(1, "0235", "0236", "Egypt")
        localDataSource.insertAlert(alert)

        val result = localDataSource.getAlerts().first()
        assertThat(1, `is`(result.size))
        assertThat(result.get(0).id, `is`(alert.id))
    }

    @Test
    fun deleteAlertById() = runTest {
        val alert = WeatherAlert(1, "0235", "0236", "Egypt")
        localDataSource.insertAlert(alert)
        localDataSource.deleteAlert(alert.id)

        val result = localDataSource.getAlerts().first()
        assertThat(result.size, `is`(0))
    }

}