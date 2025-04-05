package com.example.weather_app.data.local.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather_app.models.WeatherAlert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = database.getWeatherDao()
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun insertAlert_retrieveAlert() = runTest {
        val alert = WeatherAlert(1, "0235", "0236", "Egypt")
        dao.insertAlert(alert)

        val result = dao.getAllAlerts().first()
        assertThat(1, `is`(result.size))
        assertThat(result.get(0).id, `is`(alert.id))
    }

    @Test
    fun deleteAlertById() = runTest {
        val alert = WeatherAlert(1, "0235", "0236", "Egypt")
        dao.insertAlert(alert)
        dao.deleteAlertById(alert.id)

        val result = dao.getAllAlerts().first()
        assertThat(result.size, `is`(0))

    }

}