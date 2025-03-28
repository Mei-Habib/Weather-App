package com.example.weather_app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.R
import com.example.weather_app.ui.theme.Dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(
    titleResId: Int? = null,
    titleString: String = "",
    titleContentColor: Color = Dark,
    shouldDisplayBack: Boolean = false,
    iconTint: Color = Dark,
    onBackClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(

        navigationIcon = {
            if (shouldDisplayBack) {
                AppBarIconButton(R.drawable.ic_back, iconTint, R.string.back_icon) {
                    if (onBackClick != null) {
                        onBackClick()
                    }
                }
            }
        },

        title = {
            Text(
                text =
                if (titleResId != null)
                    stringResource(titleResId)
                else
                    titleString ?: "",

                fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                fontSize = 16.sp
            )
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = titleContentColor
        ),
    )
}

@Composable
fun AppBarIconButton(icon: Int, tint: Color, contentDescription: Int, onClick: () -> Unit) {
    Icon(
        painter = painterResource(icon),
        contentDescription = stringResource(contentDescription),
        modifier = Modifier
            .padding(10.dp)
            .size(25.dp)
            .clickable {
                onClick()
            },
        tint = tint
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherTopAppBarPreview() {
    WeatherTopAppBar(
        R.string.berlin_germany,
    ) {

    }
}