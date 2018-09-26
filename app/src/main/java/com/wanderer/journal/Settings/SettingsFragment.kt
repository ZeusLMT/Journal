package com.wanderer.journal.Settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.wanderer.journal.R

class SettingsFragment: PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)
    }
}