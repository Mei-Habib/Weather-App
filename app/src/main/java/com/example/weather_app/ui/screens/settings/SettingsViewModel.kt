package com.example.weather_app.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather_app.enums.Languages
import com.example.weather_app.enums.Locations
import com.example.weather_app.enums.Speeds
import com.example.weather_app.enums.Units
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.utils.Constants
import com.example.weather_app.utils.SharedModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SettingsViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _temp = MutableStateFlow(Units.METRIC.value)
    val temp = _temp.asStateFlow()

    private val _windSpeed = MutableStateFlow(Speeds.METER_PER_SECOND.degree)
    val windSpeed = _windSpeed.asStateFlow()

    private val _location = MutableStateFlow(Locations.GPS.enValue)
    val location = _location.asStateFlow()

    private val _language = MutableStateFlow(Languages.ENGLISH.value)
    val language = _language.asStateFlow()

    init {
        _temp.value = Units.getDegreeByValue(
            repository.getSetting(Constants.TEMP_UNIT, Units.METRIC.value)
                ?: Units.METRIC.value
        )
        SharedModel.currentDegree = _temp.value

        _windSpeed.value =
            repository.getSetting(Constants.WIND_SPEED_UNIT, Speeds.METER_PER_SECOND.degree)
                ?: Speeds.METER_PER_SECOND.degree

        _location.value = repository.getSetting(Constants.LOCATION, Locations.GPS.enValue) ?: ""

        _language.value =
            repository.getSetting(Constants.LANGUAGE_CODE, Languages.ENGLISH.code)
                ?: Languages.ENGLISH.code

    }

    fun refreshValues() {
        val unit =
            repository.getSetting(Constants.TEMP_UNIT, Units.METRIC.value) ?: Units.METRIC.value

        _temp.value = Units.getDegreeByValue(unit)
        SharedModel.currentDegree = _temp.value


        val speed =
            repository.getSetting(Constants.WIND_SPEED_UNIT, Speeds.METER_PER_SECOND.degree)
                ?: Speeds.METER_PER_SECOND.degree

        _windSpeed.value = Speeds.getDegree(speed)
        SharedModel.currentSpeed = _windSpeed.value

        val loc = repository.getSetting(Constants.LOCATION, Locations.GPS.enValue) ?: ""
        _location.value = Locations.getValue(loc)

        val code =
            repository.getSetting(Constants.LANGUAGE_CODE, Locale.getDefault().language)
                ?: Languages.ENGLISH.code
        _language.value = Languages.getValueByCode(code)

    }


    fun updateTemp(temp: String) {
        _temp.value = temp
        SharedModel.currentDegree = temp
        repository.saveSetting(Constants.TEMP_UNIT, Units.getValueByDegree(temp))

        if (Units.getValueByDegree(temp) == Units.IMPERIAL.value) {
            updateWindSpeedBasedOnTemp(Speeds.getDegree(Speeds.MILE_PER_HOUR.degree))
        } else {
            updateWindSpeedBasedOnTemp(Speeds.getDegree(Speeds.METER_PER_SECOND.degree))
        }
    }

    fun updateWindSpeed(windSpeed: String) {
        _windSpeed.value = windSpeed
        SharedModel.currentSpeed = windSpeed
        repository.saveSetting(Constants.WIND_SPEED_UNIT, Speeds.getEnglishDegree(windSpeed))

        if (Speeds.getEnglishDegree(windSpeed) == Speeds.MILE_PER_HOUR.degree) {
            updateTempBasedOnWindSpeed(Units.IMPERIAL.value)
        } else {
            updateTempBasedOnWindSpeed(Units.METRIC.value)
        }
    }

    private fun updateWindSpeedBasedOnTemp(speed: String) {
        if (_windSpeed.value != speed) {
            _windSpeed.value = speed

            SharedModel.currentSpeed = _windSpeed.value
            repository.saveSetting(Constants.WIND_SPEED_UNIT, Speeds.getEnglishDegree(speed))
        }
    }

    private fun updateTempBasedOnWindSpeed(temp: String) {
        if (temp == Units.METRIC.value &&
            (_temp.value == Units.getDegreeByValue(Units.STANDARD.value) || _temp.value == Units.getDegreeByValue(
                Units.METRIC.value
            ))
        ) {
            return
        }
        _temp.value = Units.getDegreeByValue(temp)
        repository.saveSetting(Constants.TEMP_UNIT, temp)
    }


    fun updateLocation(location: String) {
        _location.value = location
        repository.saveSetting(Constants.LOCATION, location)
    }

    fun updateLanguage(lang: String) {
        val code = Languages.getCodeByValue(lang)
        _language.value = lang
        SharedModel.currentLanguage = lang
        repository.saveSetting(Constants.LANGUAGE_CODE, code)
        Log.i("TAG", "updateLanguage: ${_language.value}")

    }

}


class SettingsFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}