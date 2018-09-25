package com.wanderer.journal

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.wanderer.journal.Timeline.TimelineFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val timelineFragment = TimelineFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize Shared Preference
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)

        //Open timeline fragment
        supportFragmentManager.beginTransaction().add(R.id.FrameLayout_mainscreen, timelineFragment).commit()

        fab_add.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java).apply{}
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }
}
