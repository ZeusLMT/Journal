package com.wanderer.journal.dataStorage

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface PostDao{
    @Query("SELECT * FROM post")
    fun getAll(): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post): Long

    @Update
    fun update(post: Post)

    @Query("SELECT * FROM post WHERE post.time = :time")
    fun getSinglePost(time: String): Post

    @Delete
    fun deletePost(post: Post)
}