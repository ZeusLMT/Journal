package com.wanderer.journal.Location

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUpdate {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var locc = ""
    fun onUpdateLocation(context: Context, activity: Activity){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        Log.d("WWWW", locc)
        if((ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location != null){
                Log.d("GEOLOCATION", "latitude: ${location.latitude}, longtitude: ${location.longitude}")
                locc = location.latitude.toString()
            }
            else Log.d("GEOLOCATION", "NULL")
        }
    }
}