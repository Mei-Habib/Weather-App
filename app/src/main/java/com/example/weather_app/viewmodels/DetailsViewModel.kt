package com.example.weather_app.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.remote.Response
import com.example.weather_app.location.LocationManager
import com.example.weather_app.models.WeatherDetails
import com.example.weather_app.repository.WeatherRepository
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: WeatherRepository,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private var _weatherData = MutableLiveData<Response<WeatherDetails>>(Response.Loading)
    val weatherData: LiveData<Response<WeatherDetails>> = _weatherData


    private val _message = MutableLiveData("")
    val message = _message.value

    init {
        fetchLocation()
    }

    private fun fetchLocation() {
        locationManager.getCurrentLocation { loc ->
            _location.postValue(loc)
            Log.i("TAG", "Location [lon: ${loc.longitude}, lat: ${loc.latitude}]")
            getWeatherDetails(loc.latitude, loc.longitude)
        }
    }

    fun getWeatherDetails(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val weatherResponse = repository.getCurrentWeather(lat, lon, "metric", "en")
                val forecastResponse = repository.getForecast(lat, lon, "metric", "en")
                val weatherDetails = WeatherDetails(weatherResponse, forecastResponse)
                _weatherData.postValue(Response.Success(weatherDetails))
            } catch (e: Exception) {
                _message.postValue(e.message)
                _weatherData.postValue(Response.Failure(e))
            }

        }
    }
}

class DetailsFactory(
    private val repository: WeatherRepository,
    private val locationManager: LocationManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository, locationManager) as T
    }
}