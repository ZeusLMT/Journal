package com.wanderer.journal.Timeline

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
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
import android.widget.Toast
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.DataStorage.PostModel
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment: Fragment() {
    private lateinit var sp: SharedPreferences
    private lateinit var postModelProvider: PostModel
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
        Log.d("abc", "Timeline onCreate")
        setUpRecyclerView()
    }

    private fun onItemClick(item: Post) {
        Toast.makeText(context, item.description, Toast.LENGTH_SHORT).show()
        //Pass Post to MainActivity
        activityCallBack!!.onItemClick(item)
    }

    private fun setUpRecyclerView() {
        val viewMode = sp.getString(getString(R.string.prefs_key_view_mode), "GRID")

        postModelProvider.getAllPosts().observe(this, Observer {
            recyclerView_timeline.adapter = TimelineAdapter(it!!.sorted(), context!!) {item: Post -> onItemClick(item)}
            recyclerView_timeline.setHasFixedSize(true)
            if (viewMode == "LIST") {
                recyclerView_timeline.layoutManager = LinearLayoutManager(context)
            } else {
                recyclerView_timeline.layoutManager = GridLayoutManager(context, sp.getString(getString(R.string.prefs_key_grid_span), "2").toInt())
            }
        })
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        activityCallBack =context as TimelineFragListener
    }
}