package com.example.weather_app.ui.screens.alerts

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weather_app.WeatherWorkManager
import com.example.weather_app.data.remote.Response
import com.example.weather_app.location.LocationUtils
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.models.WeatherAlert.Companion.toJson
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.utils.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AlertViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _alerts = MutableStateFlow<Response<List<WeatherAlert>>>(Response.Loading)
    val alerts = _alerts.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _currentAddress = MutableStateFlow("")
    val currentAddress = _currentAddress.asStateFlow()

    fun getAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            _alerts.value = Response.Loading
            repository.getAlerts()
                .catch {
                    _alerts.value = Response.Failure(it)
                }
                .collect {
                    _alerts.value = Response.Success(it)
                }
        }
    }

    fun insertAlert(alert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertAlert(alert)
            if (result > 0) {
                _message.emit("Alert Added Successfully")
            } else {
                _message.emit("Failed to add alert")
            }
        }
    }

    fun deleteAlert(alert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteAlert(alert.id)
            if (result > 0) {
                _message.value = "Alert deleted successfully"
            } else {
                _message.value = "Failed to delete alert"
            }
        }
    }

    fun loadCurrentAddress(context: Context) {
        val locationUtils = LocationUtils(context)

        if (!locationUtils.hasLocationPermission()) {
            _currentAddress.value = "Permission not granted"
            return
        }

        if (!locationUtils.isLocationEnabled()) {
            _currentAddress.value = "Location is disabled"
            return
        }

        locationUtils.getCurrentLocation { location ->
            val address = locationUtils.getAddressFromLocation(location)
            _currentAddress.value = address
        }
    }
}


class AlertFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModel(repository) as T
    }
}