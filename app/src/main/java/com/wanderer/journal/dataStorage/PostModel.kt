package com.wanderer.journal.dataStorage

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class PostModel(mApplication: Application): AndroidViewModel(mApplication){
    private val postDatabase = PostDB.get(getApplication())
    private var allPosts: LiveData<List<Post>> = postDatabase.postDao().getAll()

    fun getAllPosts() = allPosts
    fun getSinglePost(time: String): Post{
        return postDatabase.postDao().getSinglePost(time)
    }
}