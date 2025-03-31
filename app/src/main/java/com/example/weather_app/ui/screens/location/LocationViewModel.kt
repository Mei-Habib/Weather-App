package com.example.weather_app.ui.screens.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.data.remote.Response
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _favoriteLocations =
        MutableStateFlow<Response<List<FavoriteLocation>>>(Response.Loading)
    val favoriteLocations = _favoriteLocations.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun getFavoriteLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getFavoriteLocations()
                    .catch {
                        _favoriteLocations.value = Response.Failure(it)
                    }
                    .collect {
                        _favoriteLocations.value = Response.Success(it)
                    }
            } catch (e: Exception) {
                _favoriteLocations.value = Response.Failure(e)
            }
        }
    }

    fun deleteFavoriteLocation(location: FavoriteLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.deleteFavoriteLocation(location)
                if (result > 0) {
                    _message.emit("Location Deleted Successfully")
                } else {
                    _message.emit("Something went wrong")
                }
            } catch (e: Exception) {
                _message.emit(e.message.toString())
            }
        }
    }
}


class LocationFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(repository) as T
    }
}