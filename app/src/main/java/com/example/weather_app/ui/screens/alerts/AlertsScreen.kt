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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.weather_app.R
import com.example.weather_app.components.AlertBottomSheet
import com.example.weather_app.components.WeatherFloatingActionButton
import com.example.weather_app.components.WeatherTopAppBar
import com.example.weather_app.data.remote.Response
import com.example.weather_app.enums.AlertType
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.ui.theme.Dark
import com.example.weather_app.ui.theme.Grey
import com.example.weather_app.utils.cancelAlarm
import com.example.weather_app.utils.convertArabicToEnglish
import com.example.weather_app.utils.durationFromNowInSeconds
import com.example.weather_app.utils.setAlarm
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(viewModel: AlertViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    val alerts = viewModel.alerts.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val permission = Manifest.permission.POST_NOTIFICATIONS
    val permissionState = remember { mutableStateOf(isPermissionGranted(context, permission)) }
    val permanentlyDenied = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionState.value = isGranted
        if (isGranted) {
            isSheetOpen = true
        } else {
            permanentlyDenied.value = !shouldShowRequestPermissionRationale(context, permission)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.permission_denied),
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            permissionState.value = isPermissionGranted(context, permission)

        }
    }

    LaunchedEffect(Unit) { viewModel.getAlerts() }

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
                        permissionState.value -> isSheetOpen = true
                        permanentlyDenied.value -> openAppSettings(context)
                        else -> launcher.launch(permission)
                    }
                }
            )
        },
        containerColor = Grey
    ) { paddingValues ->

        when (alerts.value) {
            is Response.Failure -> EmptyAlarmsView()
            Response.Loading -> CircularProgressIndicator()
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
//                            viewModel.deleteAlert(it)
                            list -= it
                            when (it.type) {
                                AlertType.ALARM -> {
                                    context.cancelAlarm(it.id)
                                }

                                AlertType.NOTIFICATION -> {
//                                    context.cancelNotification(it.id)
                                }
                            }
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
                }) { startDuration, endDuration, selectedOption ->
                    val id = System.currentTimeMillis().toInt()
                    val alert = WeatherAlert(
                        id = id,
                        startTime = startDuration.convertArabicToEnglish(),
                        endTime = endDuration.convertArabicToEnglish(),
                        type = selectedOption
                    )
                    val startDuration = startDuration.durationFromNowInSeconds()
                    val endDuration = endDuration.durationFromNowInSeconds()
                    Log.i("TAG", "Alert Scheduled within : $startDuration seconds")
                    Log.i("TAG", "Alert Stop within : $endDuration seconds")
                    viewModel.insertAlert(alert)
                    viewModel.getAlerts()
                    when (selectedOption) {
                        AlertType.ALARM -> {
                            context.setAlarm(startDuration, id, endDuration)
                        }

                        AlertType.NOTIFICATION -> {
//                            context.scheduleNotification(id, startDuration)
                        }
                    }
                    isSheetOpen = false
                }
            }
        }
    }
}

private fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

private fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}