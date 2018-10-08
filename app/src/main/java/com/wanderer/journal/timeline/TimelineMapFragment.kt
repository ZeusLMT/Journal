package com.wanderer.journal.timeline

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.wanderer.journal.R
import com.wanderer.journal.dataStorage.Post
import com.wanderer.journal.dataStorage.PostModel


class TimelineMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var timelineMap: GoogleMap
    private var activityCallBack: TimelineMapFragListener? = null

    interface TimelineMapFragListener {
        fun onMarkerClick(timestamp: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map_timeline) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val postModelProvider = ViewModelProviders.of(activity!!).get(PostModel::class.java)
        postModelProvider.getAllPosts().observe(this, Observer {
            updateMap(it)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityCallBack = context as TimelineMapFragListener
    }

    override fun onMapReady(googleMap: GoogleMap) {
        timelineMap = googleMap
        timelineMap.setOnMarkerClickListener {
            activityCallBack!!.onMarkerClick(it.title)
            return@setOnMarkerClickListener true
        }
    }

    private fun updateMap(posts: List<Post>?) {
        Log.d("updateMap", posts.toString())
        if (posts != null) {
            Log.d("updateMap", "update map")
            timelineMap.clear()
            val builder = LatLngBounds.Builder()
            for (post in posts) {
                val location = LatLng(post.location.latitude.toDouble(), post.location.longitude.toDouble())
                val newMarker = timelineMap.addMarker(MarkerOptions()
                        .position(location)
                        .title(post.time)
                        .icon(createIcon(post.image)))
                builder.include(newMarker.position)
            }
            val bounds = builder.build()
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, dpToPx(40))
            timelineMap.moveCamera(cameraUpdate)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun createIcon(imagePath: String): BitmapDescriptor {
        val image = BitmapFactory.decodeFile(imagePath)
        val thumbnail = ThumbnailUtils.extractThumbnail(image, 150, 150)
        return BitmapDescriptorFactory.fromBitmap(thumbnail)
    }
}