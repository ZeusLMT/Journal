package com.wanderer.journal.Timeline

import android.content.Context
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.R

class TimelineAdapter(private val dataset: List<Post>, private val appContext: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class GridViewHolder(private val itemView: View,
                         val locationTextView: TextView = itemView.findViewById(R.id.location),
                         val imageView: ImageView = itemView.findViewById(R.id.imageView_photo)) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val sp = PreferenceManager.getDefaultSharedPreferences(appContext)
        var viewHolder: RecyclerView.ViewHolder? = null
        when (sp.getBoolean(appContext.getString(R.string.prefs_key_view_mode), false)) {
            true -> {
//                val itemView = LayoutInflater.from(parent.context)
//                        .inflate(R.layout.timeline_list_item, parent, false)
//                viewHolder = ListViewHolder(itemView)
            }
            false -> {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.timeline_grid_item, parent, false)
                viewHolder = GridViewHolder(itemView)
            }
        }
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val thisPost = dataset[position]
        val sp = PreferenceManager.getDefaultSharedPreferences(appContext)
        when (sp.getBoolean(appContext.getString(R.string.prefs_key_view_mode), false)) {
            true -> {
            }
            false -> {
                val imageBitmap = BitmapFactory.decodeFile(thisPost.image)
                (holder as GridViewHolder).imageView.setImageBitmap(imageBitmap)
                holder.imageView.contentDescription = appContext.getString(R.string.general_img_desc, thisPost.location)
                holder.locationTextView.text = thisPost.location
            }
        }
    }
}