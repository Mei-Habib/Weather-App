package com.example.weather_app.ui.screens.map

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.remote.Response
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLocation: State<LatLng?> = _userLocation

    private val _selectedLocation = mutableStateOf<LatLng?>(null)
    val selectedLocation: State<LatLng?> = _selectedLocation

    private val _insertion = MutableStateFlow<Response<Boolean>?>(null)
    val insertion = _insertion.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        _userLocation.value = userLatLng
                    }
                }
            } catch (e: SecurityException) {
                Log.e("TAG", "Permission for location access was revoked: ${e.localizedMessage}")
            }
        } else {
            Log.e("TAG", "Location permission is not granted.")
        }
    }

    fun selectLocation(selectedPlace: String, context: Context) {
        viewModelScope.launch {
            val geocoder = Geocoder(context)
            val addresses = withContext(Dispatchers.IO) {
                geocoder.getFromLocationName(selectedPlace, 1)
            }
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val latLng = LatLng(address.latitude, address.longitude)
                _selectedLocation.value = latLng
            } else {
                Log.e("TAG", "No location found for the selected place.")
            }
        }
    }

    fun insertFavoriteLocation(location: FavoriteLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.insertFavoriteLocation(location)
                if (result > 0) {
                    _insertion.value = Response.Success(true)
                } else {
                    _insertion.value = Response.Failure(Exception("Location already exist"))
                }
            } catch (e: Exception) {
                _insertion.value = Response.Failure(e)
                _message.emit(e.message.toString())
            }
        }
    }
}

class MapFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}
