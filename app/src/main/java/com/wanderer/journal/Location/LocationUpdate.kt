package com.wanderer.journal.Location

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUpdate {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun onUpdateLocation(context: Context, activity: Activity){
        if((Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(activity,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location != null){
                Log.d("GEOLOCATION", "latitude: ${location.latitude}, longtitude: ${location.longitude}")
            }
            else Log.d("GEOLOCATION", "NULL")
        }
    }
}