package com.wanderer.journal

import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.Settings.SettingsActivity
import com.wanderer.journal.SinglePost.DeleteDialogFragment
import com.wanderer.journal.SinglePost.SinglePostFragment
import com.wanderer.journal.Timeline.TimelineFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(), TimelineFragment.TimelineFragListener, DeleteDialogFragment.DeleteDialogListener{
    companion object {
        const val REQUEST_SETTINGS = 1
    }
    private val timelineFragment = TimelineFragment()
    private val singlePostFrag = SinglePostFragment()
    private lateinit var curPost: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        //Initialize Shared Preference and set up theme
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
        changeTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Wonderful", "Wonderful")

        //Open timeline fragment if not already there
        if (savedInstanceState == null) supportFragmentManager.beginTransaction().add(R.id.FrameLayout_mainscreen, timelineFragment).commit()
        Log.d("abc", "Main onCreate")
        fab_add.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java).apply{}
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.action_settings -> {
            val intent = Intent(this, SettingsActivity::class.java).apply{}
            startActivityForResult(intent, REQUEST_SETTINGS)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_SETTINGS -> {
                recreate()
            }
        }
    }

    private fun changeTheme() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        //Change app theme accordingly to user settings
        when (sp.getString(getString(R.string.prefs_key_app_theme), "LIGHT")) {
            "LIGHT" -> setTheme(R.style.AppTheme_PastelLight)
            "DARK" -> setTheme(R.style.AppTheme_PastelDark)
        }
    }

    override fun onItemClick(item: Post) {
        //Save current post
        curPost = item
        //Pass to SinglePostFragment
        val bundle = Bundle()
        bundle.putString("timeStamp", item.time)
        singlePostFrag.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.FrameLayout_mainscreen, singlePostFrag).addToBackStack(null).commit()
    }

    override fun onDeletePositiveClick(dialog: DialogFragment) {
        Log.d("DeleteDial", "positive")
        //File(curPost.image).delete()
        //supportFragmentManager.beginTransaction().replace(R.id.FrameLayout_mainscreen, timelineFragment).commit()
    }

    override fun onDeleteNegativeClick(dialog: DialogFragment) {
        Log.d("DeleteDial", "negative")
    }
}
