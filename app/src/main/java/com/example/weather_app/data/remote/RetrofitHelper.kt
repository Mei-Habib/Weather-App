package com.example.weather_app.data.remote

import com.example.weather_app.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .url(
                    chain.request().url.newBuilder()
                        .addQueryParameter("appid", Constants.API_KEY)
                        .build()
                )
                .build()
            chain.proceed(request)
        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiServices = retrofit.create(WeatherApiServices::class.java)

}