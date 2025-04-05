package com.example.weather_app

import com.example.weather_app.data.local.IWeatherLocalDataSource
import com.example.weather_app.data.remote.IWeatherRemoteDataSource
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.repository.IWeatherRepository
import com.example.weather_app.repository.WeatherRepository
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class WeatherRepositoryTest {

    private val alert1 = WeatherAlert(1, "0235", "0236", "Egypt")
    private val alert2 = WeatherAlert(2, "1235", "1236", "Japan")
    private val alert3 = WeatherAlert(3, "1135", "1136", "Germany")
    private val alerts = mutableListOf(alert1, alert2, alert3)

    private lateinit var fakeLocalDataSource: FakeWeatherDataSource
    private lateinit var fakeRemoteDataSource: IWeatherRemoteDataSource
    private lateinit var repository: IWeatherRepository

    @Before
    fun setup() {
        fakeLocalDataSource = FakeWeatherDataSource(alerts)
        fakeRemoteDataSource = mockk()
        repository = WeatherRepository.getInstance(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun insertAlert_retrieveAlerts() = runTest {
        val alert1 = WeatherAlert(1, "0235", "0236", "Egypt")
        val alert2 = WeatherAlert(9, "0235", "0236", "Egypt")
        val result1 = repository.insertAlert(alert1)
        val result2 = repository.insertAlert(alert2)

        val alerts = repository.getAlerts().first()
        assertTrue(result1 > 0)
        assertTrue(result2 > 0)
        assertTrue(alerts.contains(alert2))
    }

    @Test
    fun deleteAlertById() = runTest {
        val result1 = repository.deleteAlert(alert1.id)
        val result2 = repository.deleteAlert(6)

        val list = repository.getAlerts().first()
        assertTrue(result1 > 0)
        assertTrue(result2 == 0)
        assertTrue(!list.contains(alert1))
    }
}