package com.wanderer.journal.DataStorage

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class PostModel(mApplication: Application): AndroidViewModel(mApplication){
    private val postDatabase = PostDB.get(getApplication())
    private var allPosts: LiveData<List<Post>> = postDatabase.postDao().getAll()

    fun getAllPosts() = allPosts
}