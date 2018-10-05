package com.wanderer.journal.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.wanderer.journal.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        //Get app SharedPreference
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        changeTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        fragmentManager.beginTransaction().add(R.id.FrameLayout_settings, SettingsFragment()).commit()

        //Create Preference Listener and register
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                getString(R.string.prefs_key_app_theme) -> {
                    recreate()
                }
            }
        }
        sp.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun changeTheme() {
        //Change app theme accordingly to user settings
        when (sp.getString(getString(R.string.prefs_key_app_theme), "LIGHT")) {
            "LIGHT" -> setTheme(R.style.AppTheme_PastelLight)
            "DARK" -> setTheme(R.style.AppTheme_PastelDark)
        }
    }
}
