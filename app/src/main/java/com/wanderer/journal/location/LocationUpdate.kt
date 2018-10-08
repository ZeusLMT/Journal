package com.wanderer.journal.location

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationUpdate {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var latLocation: Double = 0.0
    var lonLocation: Double = 0.0
    var trueLocationCity = ""
    var trueLocationCountry = ""
    var trueLocationNeighbourhood = ""

    fun onUpdateLocation(context: Context, activity: Activity){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if((ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location != null){
                Log.d("GEOLOCATION", "latitude: ${location.latitude}, longtitude: ${location.longitude}")
                latLocation = location.latitude
                lonLocation = location.longitude

                requestLocationService(location.latitude.toString(), location.longitude.toString())

            }
            else Log.d("GEOLOCATION", "NULL")
        }
    }


    private fun requestLocationService(lat: String, long: String){
        val call = OpenStreetMapApi.locationService.getReverseGeocode(lat, long)
        val value = object : Callback<OpenStreetMapApi.Model.Result>{
            override fun onResponse(call: Call<OpenStreetMapApi.Model.Result>?, response: Response<OpenStreetMapApi.Model.Result>?) {
                if(response!= null){
                    val address= response.body()!!.address
                    trueLocationCity = address.city
                    trueLocationCountry = address.country
                    trueLocationNeighbourhood = if (address.neighbourhood != null) {
                        address.neighbourhood!!
                    } else {
                        address.suburb
                    }
                } else Log.d("LocationReverseGeocode", "null")
            }

            override fun onFailure(call: Call<OpenStreetMapApi.Model.Result>?, t: Throwable?) {
                Log.d("LocationFailure", t.toString())
            }
        }
        call.enqueue(value)
    }
}