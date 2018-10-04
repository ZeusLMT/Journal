package com.wanderer.journal.Settings

import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.ListPreference
import android.preference.PreferenceFragment
import com.wanderer.journal.R

class SettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)

        val viewModePref = findPreference(getString(R.string.prefs_key_view_mode)) as ListPreference
        val gridSpanPref = findPreference(getString(R.string.prefs_key_grid_span)) as ListPreference
        val usernamePref = findPreference(getString(R.string.prefs_key_username)) as EditTextPreference

        //check grid mode is on then enable the follow up settings
        gridSpanPref.isEnabled = viewModePref.value == "GRID"

        usernamePref.summary = usernamePref.text

        viewModePref.setOnPreferenceChangeListener { _, value ->
            gridSpanPref.isEnabled = value.toString() == "GRID"
            return@setOnPreferenceChangeListener true
        }


        usernamePref.setOnPreferenceChangeListener { _, value ->
            usernamePref.summary = value.toString()
            return@setOnPreferenceChangeListener true
        }
    }
}