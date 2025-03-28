package com.example.weather_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.weather_app.models.ScreenMenuItem
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

object BottomNavBar {

    val mutableNavBarState = MutableLiveData(true)
    private var activeIndex = 0

    @Composable
    fun ShowBottomNavBar(navController: NavHostController) {
        AndroidView(
            factory = { context ->
                CurvedBottomNavigationView(context).apply {

                    val cbnMenuItems = ScreenMenuItem.menuItems.map { screen ->
                        CbnMenuItem(
                            icon = screen.icon,
                            avdIcon = screen.selectedIcon,
                            destinationId = screen.id
                        )
                    }
                    setMenuItems(cbnMenuItems.toTypedArray(), activeIndex)
                    setOnMenuItemClickListener { _, i ->
                        activeIndex = i
                        navController.popBackStack()
                        navController.navigate(ScreenMenuItem.menuItems[i].route)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = Color.Transparent)
        )
    }
}

