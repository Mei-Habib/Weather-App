package com.example.weather_app.ui.screens.alerts


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkManager
import com.example.weather_app.R
import com.example.weather_app.components.AlertBottomSheet
import com.example.weather_app.components.CustomProgressIndicator
import com.example.weather_app.components.WeatherFloatingActionButton
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.createWeatherNotification
import com.example.weather_app.data.remote.Response
import com.example.weather_app.enums.AlertType
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.receivers.scheduleAlarm
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.utils.toMillis
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AlertsScreen(viewModel: AlertViewModel) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    val alerts = viewModel.alerts.collectAsStateWithLifecycle()
    val message = viewModel.message.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val workManager by lazy {
        WorkManager.getInstance(context)
    }

    val scope = rememberCoroutineScope()
    val postNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    var hasNotificationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permanentlyDenied = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if (!isGranted) {
                permanentlyDenied.value = !shouldShowRequestPermissionRationale(
                    context,
                    postNotificationPermission.permission
                )
            } else {
                isSheetOpen = true
            }
        }
    )

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }


    LaunchedEffect(alerts.value) { viewModel.getAlerts() }

    Scaffold(
        topBar = {
            WeatherTopAppBar(
                titleResId = R.string.scheduled_alerts,
                titleContentColor = Dark,
                iconTint = Dark,
            ) {

            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            WeatherFloatingActionButton(
                action = {
                    when {
                        hasNotificationPermission -> isSheetOpen = true
                        permanentlyDenied.value -> openAppSettings(context)
                        else -> permissionLauncher.launch(postNotificationPermission.permission)
                    }
                }
            )
        },
        containerColor = Grey
    ) { paddingValues ->

        when (alerts.value) {
            is Response.Failure -> EmptyAlarmsView()
            Response.Loading -> CustomProgressIndicator()
            is Response.Success -> {
                val data = (alerts.value as Response.Success).response
                var list by remember { mutableStateOf(data) }

                if (data.isEmpty()) {
                    EmptyAlarmsView()
                } else {
                    Box(
                        modifier = Modifier
                            .padding(top = paddingValues.calculateTopPadding())
                            .background(
                                Grey
                            )
                    ) {
                        AlertsListView(alerts = list, snackbarHostState = snackbarHostState) {
                            viewModel.deleteAlert(it)
                            list -= it
                        }
                    }
                }
            }
        }

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState, containerColor = Color.White
            ) {
                AlertBottomSheet(onClose = {
                    isSheetOpen = false
                }) { startDuration, endDuration ->
                    val alert = WeatherAlert(
                        startDuration = startDuration,
                        endDuration = endDuration,
                    )
                    viewModel.insertAlert(alert)
                    viewModel.scheduleWeatherAlert(workManager, alert)

                    createWeatherNotification(context, "Weather Notification")
                    isSheetOpen = false
                }
            }
        }
    }
}

private fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return shouldShowRequestPermissionRationale(context as Activity, permission)
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}