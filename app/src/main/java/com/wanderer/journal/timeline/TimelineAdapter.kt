package com.wanderer.journal.timeline

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wanderer.journal.R
import com.wanderer.journal.dataStorage.Post

class TimelineAdapter (private val appContext: Context, val clickListener: (Post) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataset: List<Post> = emptyList()

    class GridViewHolder (private val itemView: View,
                         val locationTextView: TextView = itemView.findViewById(R.id.location),
                         val imageView: ImageView = itemView.findViewById(R.id.imageView_grid)) : RecyclerView.ViewHolder(itemView)

    class ListViewHolder (private val itemView: View,
                          val timestampDate: TextView = itemView.findViewById(R.id.timestamp_date),
                          val timestampMonthYear: TextView = itemView.findViewById(R.id.timestamp_month_year),
                          val imageView: ImageView = itemView.findViewById(R.id.imageView_list),
                          val dividerTop: View = itemView.findViewById(R.id.divider_top),
                          val dividerBottom: View = itemView.findViewById(R.id.divider_bottom)) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val sp = PreferenceManager.getDefaultSharedPreferences(appContext)
        var viewHolder: RecyclerView.ViewHolder? = null
        when (sp.getString(appContext.getString(R.string.prefs_key_view_mode), "GRID")) {
            "LIST" -> {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.timeline_list_item, parent, false)
                viewHolder = ListViewHolder(itemView)
            }
            "GRID" -> {
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

        when (sp.getString(appContext.getString(R.string.prefs_key_view_mode), "GRID")) {
            "LIST" -> {
                (holder as ListViewHolder).imageView.contentDescription = appContext.getString(R.string.general_img_desc, thisPost.location.city)
                //holder.timestampDate.text = thisPost.time.substringBefore(" ")
                setTimestamp(holder.timestampMonthYear, holder.timestampDate, thisPost.time)
                holder.imageView.setImageBitmap(scaleImage(thisPost.image))

                if (position == 0) {
                    holder.dividerTop.visibility = View.GONE
                }

                if(position >= 1 && comparePost(thisPost, dataset[position-1])) {
                    Log.d("onBindViewHolder", "GONE")
                    holder.timestampDate.visibility = View.GONE
                    holder.timestampMonthYear.visibility = View.GONE
                    holder.dividerTop.visibility = View.GONE
                }

                if (position == dataset.lastIndex) {
                    holder.dividerBottom.visibility = View.GONE
                }

                holder.imageView.setOnClickListener {clickListener(thisPost)}
            }
            "GRID" -> {
                //(holder as GridViewHolder).imageView.setImageBitmap(imageBitmap)
                (holder as GridViewHolder).imageView.contentDescription = appContext.getString(R.string.general_img_desc, thisPost.location.city)
                holder.locationTextView.text = thisPost.location.city
                holder.itemView.setOnClickListener {clickListener(thisPost)}
                holder.imageView.setImageBitmap(squareCropImg(scaleImage(thisPost.image)))
            }
        }
    }

    fun setData(newData: List<Post>) {
        dataset = newData
        notifyDataSetChanged()
    }

    private fun comparePost(thisPost: Post, otherPost: Post): Boolean {
        return thisPost.time.substringBefore(" ") == otherPost.time.substringBefore(" ")
    }

    private fun squareCropImg(original: Bitmap): Bitmap {
        val width = original.width
        val height = original.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH
        return Bitmap.createBitmap(original, cropW, cropH, newWidth, newHeight)
    }

    private fun scaleImage(imagePath: String): Bitmap {
        // Get the dimensions of the View
        val targetW = 720

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight
            val ratio = photoW.toFloat() / photoH.toFloat()
            val targetH = (targetW / ratio).toInt()
            Log.d("scaledImageInfo", "$photoW, $photoH, $ratio, $targetW, $targetH")

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        return BitmapFactory.decodeFile(imagePath, bmOptions)
    }

    private fun setTimestamp(textViewMonthYear: TextView, textViewdate: TextView, timestamp: String) {
        val dateMonthYear = timestamp.substringBefore(" - ")
        Log.d("abc", dateMonthYear)
        val monthYear = dateMonthYear.substringAfter(" ")
        Log.d("abc", monthYear)

        textViewdate.text = appContext.getString(R.string.date, dateMonthYear.substringBefore(" "))
        textViewMonthYear.text = appContext.getString(R.string.month_and_year, monthYear.substringBefore(" "), monthYear.substringAfter(" "))
    }
}