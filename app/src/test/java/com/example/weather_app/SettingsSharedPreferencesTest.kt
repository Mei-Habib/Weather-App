package com.example.weather_app

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather_app.data.local.sharedpreferences.ISettingsSharedPreferences
import com.example.weather_app.data.local.sharedpreferences.SettingsSharedPreferences
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class SettingsSharedPreferencesTest {

    private lateinit var settingsPrefs: ISettingsSharedPreferences
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Application>()
        settingsPrefs = SettingsSharedPreferences.getInstance(context)
    }

    @Test
    fun saveSetting_getSetting_isCorrect() {
        settingsPrefs.saveSetting("temp", "metric")

        val result = settingsPrefs.getSetting("temp", "imperial")
        assertThat(result, `is`("metric"))
    }

    @Test
    fun getSetting_nonExistingKey_returnDefault() {
        val result = settingsPrefs.getSetting("speed", "mph")
        assertThat(result, `is`("mph"))
    }
}