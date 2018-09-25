package com.wanderer.journal

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.DataStorage.PostDB
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent(this, PostActivity::class.java).apply{}
//        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }
}
