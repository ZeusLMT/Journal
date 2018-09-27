package com.wanderer.journal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wanderer.journal.SinglePost.SinglePostFragment

class SinglePostActivity : AppCompatActivity() {
    private val singlePostFrag = SinglePostFragment()
    private lateinit var time: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)
        val extras = intent.extras
        time = extras.getString("timeStamp")

        val bundle = Bundle()
        bundle.putString("timeStamp", time)
        singlePostFrag.arguments = bundle
        if (savedInstanceState == null) supportFragmentManager.beginTransaction().add(R.id.single_view_frame, singlePostFrag).commit()
    }
}
