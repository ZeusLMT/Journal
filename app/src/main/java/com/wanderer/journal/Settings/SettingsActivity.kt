package com.wanderer.journal.Settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wanderer.journal.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        fragmentManager.beginTransaction().add(R.id.FrameLayout_settings, SettingsFragment()).commit()
    }
}
