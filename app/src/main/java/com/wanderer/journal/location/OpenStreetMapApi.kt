package com.wanderer.journal.location

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object OpenStreetMapApi {
    private const val URL = "https://nominatim.openstreetmap.org/"

    object Model{
        data class Result(var address: Address)
        data class Address(var city: String, var country: String, var neighbourhood: String)
    }

    interface LocationService{
        @GET("reverse?format=json&zoom=20&addressdetails=1")
        fun getReverseGeocode(@Query("lat") lat: String,
                              @Query("lon")long: String): Call<Model.Result>
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val locationService = retrofit.create(LocationService::class.java)!!
}