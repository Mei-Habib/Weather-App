package com.example.weather_app.location

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(app: Application) : AndroidViewModel(app) {
    private val locationUtils = LocationUtils(app.applicationContext)

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    fun fetchLocation() {
        viewModelScope.launch {
            locationUtils.getCurrentLocation { loc ->
                _location.value = loc
            }
        }
    }
}