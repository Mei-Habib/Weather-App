package com.example.weather_app.ui.screens.locations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.ui.theme.BabyBlue
import com.example.weather_app.ui.theme.Blue
import com.example.weather_app.utils.CountryMapper
import com.example.weather_app.utils.IconsMapper

@Composable
fun FavoriteLocationCard(location: FavoriteLocation, onSelected: (FavoriteLocation) -> Unit) {
    val brush = Brush.horizontalGradient(listOf(Blue, BabyBlue))

    Row(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(brush)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable { onSelected(location) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                IconsMapper.iconsMap.get(location.weather.weather.weather.firstOrNull()?.icon)
                    ?: R.drawable.partly_sunny
            ),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(45.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${location.city}, ${CountryMapper.getCountryNameFromCode(location.counter)}",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins_semi_bold))
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "${location.weather.weather.weather.firstOrNull()?.description}",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular))
            )
        }

        Text(
            text = "${location.weather.weather.main.temp.toInt()}°",
            color = Color.White,
            fontSize = 36.sp,
            fontFamily = FontFamily(Font(R.font.poppins_light))
        )
    }
}

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    snackbarHostState: SnackbarHostState,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    val context = LocalContext.current
    var isRemoved by remember { mutableStateOf(false) }
    var showItem by remember { mutableStateOf(true) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                showItem = false
                true
            } else false
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(R.string.deleted_successfully),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed) {
                isRemoved = false
                showItem = true
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            } else {
                onDelete(item)
            }
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(durationMillis = animationDuration)
        ) + fadeOut()
    ) {
        if (showItem) {
            SwipeToDismissBox(
                state = swipeState,
                backgroundContent = {
                    DeleteBackground(swipeDismissState = swipeState)
                },
                enableDismissFromStartToEnd = false
            ) {
                content(item)
            }
        }
    }
}

@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color = if (swipeDismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
        Color.Red.copy(alpha = 0.5f)
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .padding(start = 24.dp)
            .background(color),
        contentAlignment = Alignment.CenterEnd
    ) {

        Icon(
            modifier = Modifier.padding(16.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White,
        )
    }

}