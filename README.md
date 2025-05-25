# Weather App
An Android application built with Kotlin that delivers real-time weather updates based on your current or selected location. Users can monitor multiple favorite cities, receive weather alerts, and personalize their preferences such as temperature units and language.

# Features
- Current Weather Display
- Real-time weather updates based on location.

- Detailed metrics: temperature, humidity, wind speed, pressure, cloud coverage, and more.

- Weather condition icons and textual descriptions.

- Hourly weather updates and 5-day forecast.
-  Favorites Screen
Add cities via map interaction or text search.

Save and revisit forecasts for your favorite locations.

Option to delete saved places.

- Alerts Screen
Configure alerts for specific weather scenarios.

Get notified at selected dates and times.

Manage alerts with snooze, dismiss, and cancel options.

- Settings Screen
Toggle between GPS-based and manual location selection.

Switch between temperature units: Celsius (°C), Fahrenheit (°F), and Kelvin.

Wind speed units: m/s or mph.

Language options: English and Arabic.

# Architecture & Tech Stack
- Architecture: MVVM (Model-View-ViewModel)

- Networking: Retrofit

- Local Storage: Room Database

- Async Operations: Kotlin Coroutines

- Background Tasks: WorkManager

- State Persistence: SharedPreferences

- Location & Maps:

- Google Maps SDK for Android

- Places API

# Testing
Unit Testing for ViewModels and core logic.
