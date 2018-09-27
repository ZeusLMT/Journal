package com.wanderer.journal.SinglePost

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderer.journal.DataStorage.Post
import com.wanderer.journal.DataStorage.PostModel
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.fragment_single_post.*

class SinglePostFragment: Fragment() {
    private lateinit var timeStamp: String
    private lateinit var postModelProvider: PostModel
    private lateinit var myPost: Post

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Get time stamp from MainActivity
        val bundle = arguments
        timeStamp = bundle!!.getString("timeStamp")
        return inflater.inflate(R.layout.fragment_single_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GetData().execute(timeStamp)
    }

    inner class GetData: AsyncTask<String, Unit, Post>(){
        override fun doInBackground(vararg params: String): Post {
            postModelProvider = ViewModelProviders.of(activity!!).get(PostModel::class.java)
            myPost = postModelProvider.getSinglePost(params[0])
            return myPost
        }

        override fun onPostExecute(result: Post) {
            Log.d("MyPostX", myPost.toString())
            val imgBitmap = squareCropImg(BitmapFactory.decodeFile(result.image))
            post_img.setImageBitmap(imgBitmap)
            description.text = result.description
            time_stamp.text = result.time

            val locationDisplay = "${result.neighbourhood}, ${result.city}, ${result.country}"
            location_stamp.text = locationDisplay

        }
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
}