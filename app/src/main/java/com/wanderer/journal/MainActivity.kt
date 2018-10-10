package com.wanderer.journal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.wanderer.journal.dataStorage.Post
import com.wanderer.journal.settings.SettingsActivity
import com.wanderer.journal.singlePost.SinglePostActivity
import com.wanderer.journal.timeline.TimelineFragment
import com.wanderer.journal.timeline.TimelineMapFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity(), TimelineFragment.TimelineFragListener, TimelineMapFragment.TimelineMapFragListener {
    companion object {
        const val REQUEST_SETTINGS = 1
        const val DEGREE = "\u00b0"
    }
    private val timelineFragment = TimelineFragment()
    private val timelineMapFragment = TimelineMapFragment()
    private lateinit var curPost: Post
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        //Set up theme
        changeTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Open timeline fragment if not already there
        if (sp.getString(getString(R.string.prefs_key_view_mode), "LIST") == "MAP") {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction().add(R.id.FrameLayout_mainscreen, timelineMapFragment).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout_mainscreen, timelineMapFragment).commit()
            }
        } else {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction().add(R.id.FrameLayout_mainscreen, timelineFragment).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout_mainscreen, timelineFragment).commit()
            }
        }

        fab_add.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java).apply {}
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu!!.findItem(R.id.action_demo)
        item.isEnabled = !sp.getBoolean(getString(R.string.prefs_key_demo), false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.action_settings -> {
            val intent = Intent(this, SettingsActivity::class.java).apply{}
            startActivityForResult(intent, REQUEST_SETTINGS)
            true
        }

        R.id.action_demo -> {
            with(sp.edit()) {
                putBoolean(getString(R.string.prefs_key_demo), true)
                apply()
            }
            doAsync {
                val demoDataUtils = DemoDataUtils(applicationContext)
                demoDataUtils.setupDemoData()
            }
            false
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

    override fun onMarkerClick(timestamp: String) {
        val intent = Intent(this, SinglePostActivity::class.java)
        intent.putExtra("timestamp", timestamp)
        startActivity(intent)
    }

}
