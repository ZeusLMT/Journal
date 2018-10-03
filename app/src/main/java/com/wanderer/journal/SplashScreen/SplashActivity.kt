package com.wanderer.journal.SplashScreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.wanderer.journal.MainActivity
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.splash_user.*

class SplashActivity : AppCompatActivity() {
    companion object {
        const val INTERVAL: Long = 2000
    }

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        //Initialize Shared Preferences
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
        PreferenceManager.setDefaultValues(this, R.xml.other_prefs, false)
        sp = PreferenceManager.getDefaultSharedPreferences(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            val firstVisit = sp.getBoolean(getString(R.string.prefs_key_first_visit), false)
            Log.d("abc", sp.getBoolean(getString(R.string.prefs_key_first_visit), false).toString())
            if (!firstVisit) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                handleUserScreen()
            }
        }, INTERVAL)
    }


    private fun handleUserScreen() {
        setContentView(R.layout.splash_user)
        imageButton_next.setOnClickListener {
            if (editText_username.text.isBlank() || editText_passcode.text.isBlank()) {
                Toast.makeText(this, "Username & passcode should not be empty", Toast.LENGTH_SHORT).show()
            } else {
                val username = editText_username.text.toString()
                val passcode = editText_passcode.text.toString()

                with(sp.edit()) {
                    putString(getString(R.string.prefs_key_username), username)
                    putString(getString(R.string.prefs_key_passcode), passcode)
                    apply()
                }

                handlePermissionScreen()
            }
        }
    }

    private fun handlePermissionScreen() {
        setContentView(R.layout.splash_permission)

    }

}
