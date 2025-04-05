package com.example.weather_app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.ui.screens.alerts.AlertViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: AlertViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = AlertViewModel(repository)
    }

}