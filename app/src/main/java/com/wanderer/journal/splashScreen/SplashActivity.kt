package com.wanderer.journal.splashScreen

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import com.wanderer.journal.MainActivity
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.splash_login.*
import kotlinx.android.synthetic.main.splash_permission.*
import kotlinx.android.synthetic.main.splash_user.*
import org.jetbrains.anko.contentView

class SplashActivity : AppCompatActivity() {
    companion object {
        const val INTERVAL: Long = 2000
    }

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Initialize Shared Preferences
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        if (!sp.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
            PreferenceManager.setDefaultValues(this, R.xml.other_prefs, true)
        }

        //Show the splash screen for 2s then continues
        val handler = Handler()
        handler.postDelayed({
            val firstVisit = sp.getBoolean(getString(R.string.prefs_key_first_visit), false)
            if (!firstVisit) {
                contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
                handleLogin()
            } else {
                contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
                handleUserScreen()
            }
        }, INTERVAL)
    }


    private fun handleUserScreen() {
        //Change to new user screen if visit 1st time
        setContentView(R.layout.splash_user)
        contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        imageButton_next_user.setOnClickListener {
            if (editText_username.text.isBlank() || editText_passcode_user.text.isBlank()) {
                if (editText_username.text.isBlank()) username_user.error = getString(R.string.username_error)

                if (editText_passcode_user.text.isBlank()) passcode_user.error = getString(R.string.password_error)
            } else {
                val username = editText_username.text.toString().replace(" ", "")
                val passcode = editText_passcode_user.text.toString()

                with(sp.edit()) {
                    putString(getString(R.string.prefs_key_username), username)
                    putString(getString(R.string.prefs_key_passcode), passcode)
                    apply()
                }
                contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out))
                handlePermissionScreen()
            }
        }
    }

    private fun handlePermissionScreen() {
        //Explain and request permission for Location
        setContentView(R.layout.splash_permission)
        contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in))

        imageButton_accept.setOnClickListener {
            //Request location permission
            if ((ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else startTimeline()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //Start main activity when user granted permission
                startTimeline()
            } else {
                //Further explain the required permission
                textView_explain.text = getString(R.string.permission_warning)
                textView_explain.setTextColor(getColor(R.color.textView_warning))
            }
            return
        }

    }

    private fun handleLogin() {
        //If not 1st visit, show login screen
        setContentView(R.layout.splash_login)
        contentView!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        val username = sp.getString(getString(R.string.prefs_key_username), "User")
        textView_greetings.text = getString(R.string.user_greetings, username)

        imageButton_next_login.setOnClickListener {
            login()
        }

        editText_passcode_login.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            return@setOnEditorActionListener false
        }
    }

    private fun login() {
        val userPasscode = sp.getString(getString(R.string.prefs_key_passcode), "0000")

        //Check user input with passcode in SharedPreference
        when {
            editText_passcode_login.text.toString() == userPasscode -> startTimeline()
            editText_passcode_login.text.isBlank() -> passcode_login.error = getString(R.string.password_blank)
            else -> passcode_login.error = getString(R.string.password_wrong)
        }
    }

    private fun startTimeline() {
        if (sp.getBoolean(getString(R.string.prefs_key_first_visit), true)) {
            with(sp.edit()) {
                putBoolean(getString(R.string.prefs_key_first_visit), false)
                apply()
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
