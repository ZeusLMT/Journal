package com.wanderer.journal

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.Settings.SettingsActivity
import com.wanderer.journal.SinglePost.SinglePostActivity
import com.wanderer.journal.Timeline.TimelineFragment

class MainActivity : AppCompatActivity(), TimelineFragment.TimelineFragListener {
    companion object {
        const val REQUEST_SETTINGS = 1
    }
    private val timelineFragment = TimelineFragment()
    private lateinit var curPost: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        //Set up theme
        changeTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Open timeline fragment if not already there
        if (savedInstanceState == null) supportFragmentManager.beginTransaction().add(R.id.FrameLayout_mainscreen, timelineFragment).commit()

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
        val intent = Intent(this, SinglePostActivity::class.java)
        intent.putExtra("timestamp", item.time)
        startActivity(intent)
    }
}
