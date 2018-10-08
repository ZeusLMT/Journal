package com.wanderer.journal.location

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WeatherApi {
    private const val URL = "https://api.openweathermap.org/"
    private const val KEY = "e6c73a05c97ed9be65c1c563256fea42"

    object Model{
        data class WeatherResult(var weather: ArrayList<Weather>, var main: Main)
        data class Weather(var description: String)
        data class Main(var temp: String)
    }

    interface LocationService{
        @GET("data/2.5/weather?appid=$KEY&units=metric")
        fun getWeatherInfo(@Query("q")q: String): Call<Model.WeatherResult>
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val weatherService = retrofit.create(LocationService::class.java)!!
}