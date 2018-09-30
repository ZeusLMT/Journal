package com.wanderer.journal.Timeline

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.DataStorage.PostModel
import com.wanderer.journal.PostActivity
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment: Fragment() {
    private lateinit var sp: SharedPreferences
    private lateinit var postModelProvider: PostModel
    private lateinit var adapter: TimelineAdapter
    private var activityCallBack: TimelineFragListener ?= null

    interface TimelineFragListener{
        fun onItemClick(item: Post)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        postModelProvider = ViewModelProviders.of(activity!!).get(PostModel::class.java)
        adapter = TimelineAdapter(context!!){item: Post -> onItemClick(item)}
        Log.d("abc", "Timeline onCreate")

        setUpRecyclerView()

        postModelProvider.getAllPosts().observe(this, Observer {
            Log.d("abc", it.toString())
            adapter.setData(it!!.sorted())
        })

        fab_add.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java).apply{}
            startActivity(intent)
        }
    }

    private fun onItemClick(item: Post) {
        //Pass post to MainActivity
        activityCallBack!!.onItemClick(item)
    }

    private fun setUpRecyclerView() {
        val viewMode = sp.getString(getString(R.string.prefs_key_view_mode), "GRID")
        if (viewMode == "LIST") {
            recyclerView_timeline.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView_timeline.layoutManager = GridLayoutManager(context, sp.getString(getString(R.string.prefs_key_grid_span), "2").toInt())
        }
        recyclerView_timeline.adapter = adapter
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        activityCallBack =context as TimelineFragListener
    }
}