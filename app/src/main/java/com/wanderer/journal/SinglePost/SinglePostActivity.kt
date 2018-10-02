package com.wanderer.journal.SinglePost

import android.app.DialogFragment
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.DataStorage.PostDB
import com.wanderer.journal.DataStorage.PostModel
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.activity_single_post.*
import kotlinx.android.synthetic.main.content_single_post.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync
import java.io.File

class SinglePostActivity : AppCompatActivity(), DeleteDialogFragment.DeleteDialogListener, OptionModalFragment.OptionModalListener {
    private lateinit var timestamp: String
    private lateinit var postModelProvider: PostModel
    private lateinit var myPost: Post
    private val newDel = DeleteDialogFragment()
    private val optionModalFragment = OptionModalFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        //Initialize Shared Preference and set up theme
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
        changeTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        fab_edit.setOnClickListener { view ->
            //showBottomSheet()
        }

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

            val locationDisplay = result.location.toString()
            textView_location.text = locationDisplay
        }
    }

    //DELETE DIALOG

    override fun onDeletePositiveClick(dialog: DialogFragment) {
        Log.d("DeleteDial", "positive")
        val postDB = PostDB.get(this)
        doAsync {
            File(myPost.image).delete()
            postDB.postDao().deletePost(myPost)
            finish()
            UI {
                Snackbar.make(view, "Journal entry deleted", Snackbar.LENGTH_LONG)
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

    private fun showBottomSheet() {
        val fm = this.supportFragmentManager
        optionModalFragment.show(fm, "option")
        Log.d("DeleteDial", "showBottomSheet")
    }

    private fun changeTheme() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        //Change app theme accordingly to user settings
        when (sp.getString(getString(R.string.prefs_key_app_theme), "LIGHT")) {
            "LIGHT" -> setTheme(R.style.AppTheme_PastelLight_NoActionBar)
            "DARK" -> setTheme(R.style.AppTheme_PastelDark_NoActionBar)
        }
    }

    override fun onOptionClick(id: String) {
        when(id){
            "delete" -> {
                val fm = this.fragmentManager
                newDel.show(fm, "Delete")
            }
            "edit" -> {
                Toast.makeText(this, "Edit clicked", Toast.LENGTH_SHORT).show()
            }
            "map" -> {
                Toast.makeText(this, "map clicked", Toast.LENGTH_SHORT).show()
            }
            "gallery" -> {
                val imgFile = File(myPost.image)
                val imgUri = FileProvider.getUriForFile(this, "com.wanderer.journal", imgFile)
                val viewIntent = Intent(Intent.ACTION_VIEW)
                if (viewIntent.resolveActivity(packageManager) != null) {
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    viewIntent.data = imgUri
                    startActivity(viewIntent)
                } else Toast.makeText(this, "Error opening app", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
