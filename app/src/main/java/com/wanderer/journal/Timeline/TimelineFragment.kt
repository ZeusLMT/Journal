package com.wanderer.journal.Timeline

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderer.journal.DataStorage.PostModel
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val viewMode = sp.getString(getString(R.string.prefs_key_view_mode), "GRID")
        val postModelProvider = ViewModelProviders.of(activity!!).get(PostModel::class.java)

        postModelProvider.getAllPosts().observe(this, Observer {
            recyclerView_timeline.adapter = TimelineAdapter(it!!.sorted(), context!!)
            if (viewMode == "LIST") {
                recyclerView_timeline.layoutManager = LinearLayoutManager(context)
            } else {
                recyclerView_timeline.layoutManager = GridLayoutManager(context, 3)
            }
        })
    }
}