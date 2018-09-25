package com.wanderer.journal

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

        val postDB = PostDB.get(this)
        doAsync {
            val x = postDB.postDao().insert(Post("2", "myImage", "myDesc", "myLoc"))
            val t = postDB.postDao().insert(Post("3", "myImages", "myDescs", "myLocs"))
            val y = postDB.postDao().getSinglePost("2")
            val z: List<Post> = postDB.postDao().getAll()
            UI {
                Log.d("Checking", "$x $y")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }
}
