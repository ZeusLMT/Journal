package com.wanderer.journal.Timeline

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderer.journal.DataStorage.PostDB
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.fragment_timeline.*

class TimelineFragment: Fragment() {
    private val sp = PreferenceManager.getDefaultSharedPreferences(context)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val allPosts = PostDB.get(context!!).postDao().getAll()
        val isList = sp.getBoolean(getString(R.string.prefs_key_view_mode), false)

        recyclerView_timeline.adapter = TimelineAdapter(allPosts.sorted, context!!)
        if (isList) {
            recyclerView_timeline.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView_timeline.layoutManager = GridLayoutManager(context, 3)
        }
    }
}