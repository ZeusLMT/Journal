package com.wanderer.journal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu

class MainActivity : AppCompatActivity() {
    private val timelineFragment = TimelineFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Open timeline fragment
        supportFragmentManager.beginTransaction().add(R.id.FrameLayout_mainscreen, timelineFragment).commit()

//        val intent = Intent(this, PostActivity::class.java).apply{}
//        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }
}
