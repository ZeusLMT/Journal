package com.wanderer.journal.singlePost

import android.app.DialogFragment
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wanderer.journal.dataStorage.Post
import com.wanderer.journal.dataStorage.PostDB
import com.wanderer.journal.dataStorage.PostModel
import com.wanderer.journal.editPost.EditPostActivity
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.activity_single_post.*
import kotlinx.android.synthetic.main.content_single_post.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync
import java.io.File

class SinglePostActivity : AppCompatActivity(), DeleteDialogFragment.DeleteDialogListener, OptionModalFragment.OptionModalListener, OnMapReadyCallback {
    private lateinit var timestamp: String
    private lateinit var postModelProvider: PostModel
    private lateinit var myPost: Post
    private val newDel = DeleteDialogFragment()
    private val optionModalFragment = OptionModalFragment()
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        //Initialize Shared Preference and set up theme
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
        changeTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        //Edit floating button
        fab_edit.setOnClickListener {
            onEdit()
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        timestamp = intent.getStringExtra("timestamp")
        GetData().execute(timestamp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_single_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.action_options -> {
            showBottomSheet()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    //Get and display single post
    inner class GetData : AsyncTask<String, Unit, Post>() {
        override fun doInBackground(vararg params: String): Post {
            postModelProvider = ViewModelProviders.of(this@SinglePostActivity).get(PostModel::class.java)
            myPost = postModelProvider.getSinglePost(params[0])
            return myPost
        }

        override fun onPostExecute(result: Post) {
            val imgBitmap = squareCropImg(BitmapFactory.decodeFile(result.image))
            header_img.setImageBitmap(imgBitmap)
            textView_description.text = result.description
            textView_date.text = result.time
            Log.d("Weather", result.weather)

            val locationDisplay = result.location.toString()
            textView_location.text = locationDisplay

        }
    }

    //Delete Dialog
    override fun onDeletePositiveClick(dialog: DialogFragment) {
        Log.d("DeleteDial", "positive")
        val postDB = PostDB.get(this)
        doAsync {
            File(myPost.image).delete()
            postDB.postDao().deletePost(myPost)
            finish()
            UI {
                Snackbar.make(view, getString(R.string.snackbar_deleted_entry), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

    override fun onDeleteNegativeClick(dialog: DialogFragment) {
        Log.d("DeleteDial", "negative")
    }

    private fun squareCropImg(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH
        return Bitmap.createBitmap(original, cropW, cropH, newWidth, newHeight)
    }

    //Display bottom option modal
    private fun showBottomSheet() {
        val fm = this.supportFragmentManager
        optionModalFragment.show(fm, "option")
        Log.d("DeleteDial", "showBottomSheet")
    }

    //Change app theme accordingly to user settings
    private fun changeTheme() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)

        when (sp.getString(getString(R.string.prefs_key_app_theme), "LIGHT")) {
            "LIGHT" -> setTheme(R.style.AppTheme_PastelLight_NoActionBar)
            "DARK" -> setTheme(R.style.AppTheme_PastelDark_NoActionBar)
        }
    }

    //Bottom modal click handler
    override fun onOptionClick(id: String) {
        when(id){
            "delete" -> {
                val fm = this.fragmentManager
                newDel.show(fm, "Delete")
            }
            "share" -> {
                Toast.makeText(this, getString(R.string.toast_share), Toast.LENGTH_SHORT).show()
            }
            "wiki" -> {
                openWiki(myPost.location.city)
            }
            "gallery" -> {
                val imgFile = File(myPost.image)
                val imgUri = FileProvider.getUriForFile(this, "com.wanderer.journal", imgFile)
                val viewIntent = Intent(Intent.ACTION_VIEW)
                if (viewIntent.resolveActivity(packageManager) != null) {
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    viewIntent.data = imgUri
                    startActivity(viewIntent)
                } else Toast.makeText(this, getString(R.string.toast_error_gallery), Toast.LENGTH_SHORT).show()
            }
        }

        optionModalFragment.dismiss()
    }

    //Set up map view
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location = LatLng(myPost.location.latitude.toDouble(), myPost.location.longitude.toDouble())
        mMap.addMarker(MarkerOptions().position(location)
                .title("Image location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.place_marker)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    //Edit button
    private fun onEdit(){
        val intent = Intent(this, EditPostActivity::class.java)
        intent.putExtra("timestamp", timestamp)
        startActivity(intent)
    }

    //Search wikipedia for location
    private fun openWiki(location: String){
        val webpage: Uri = Uri.parse("https://en.wikipedia.org/wiki/$location")
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    //
}
