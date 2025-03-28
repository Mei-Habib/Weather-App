package com.example.weather_app.components

import android.graphics.Typeface
import android.view.Gravity
import com.example.weather_app.R
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.example.weather_app.ui.theme.DarkGrey
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

@Composable
fun SearchBar(
    onPlaceSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = stringResource(R.string.search_icon),
                tint = DarkGrey,
                modifier = Modifier
                    .padding(start = 14.dp, end = 8.dp)
                    .size(20.dp)
            )

            AndroidView(
                factory = { ctx ->
                    AutoCompleteTextView(ctx).apply {
                        hint = context.getString(R.string.search_for_a_place)
                        setTextColor(Color.Black.toArgb())
                        setHintTextColor(DarkGrey.toArgb())
                        textSize = 14f
                        typeface =
                            ResourcesCompat.getFont(ctx, R.font.poppins_regular) ?: Typeface.DEFAULT
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)

                        setPadding(0, 0, 0, 0)
                        gravity = Gravity.CENTER_VERTICAL

                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        val autocompleteAdapter =
                            ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line)
                        val placesClient = Places.createClient(ctx)
                        val autocompleteSessionToken = AutocompleteSessionToken.newInstance()

                        addTextChangedListener { editable ->
                            val query = editable?.toString() ?: ""
                            if (query.isNotEmpty()) {
                                val request = FindAutocompletePredictionsRequest.builder()
                                    .setSessionToken(autocompleteSessionToken)
                                    .setQuery(query)
                                    .build()

                                placesClient.findAutocompletePredictions(request)
                                    .addOnSuccessListener { response ->
                                        autocompleteAdapter.clear()
                                        response.autocompletePredictions.forEach { prediction ->
                                            autocompleteAdapter.add(
                                                prediction.getFullText(null).toString()
                                            )
                                        }
                                        autocompleteAdapter.notifyDataSetChanged()
                                    }
                            }
                        }

                        setAdapter(autocompleteAdapter)
                        setOnItemClickListener { _, _, position, _ ->
                            val selectedPlace = autocompleteAdapter.getItem(position)
                                ?: return@setOnItemClickListener
                            onPlaceSelected(selectedPlace)
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}