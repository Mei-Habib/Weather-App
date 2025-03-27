package com.example.weather_app.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(private val context: Context) {
    private var fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context, ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun promptEnableLocation() {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }


    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        val locationRequest = LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setMaxUpdates(1) // get location once
        }.build()

        fusedClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        onLocationReceived(it)
                    }
                    fusedClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    fun shouldShowRationale(activity: Activity): Boolean {
        return activity.shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) ||
                activity.shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)
    }
}
