package com.wanderer.journal.dataStorage

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(Post::class), (Location::class)], version = 1)
abstract class PostDB : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun locationDao(): LocationDao

    companion object {
        private var sInstance: PostDB? = null
        @Synchronized
        fun get(context: Context): PostDB {
            if (sInstance == null) {
                sInstance =
                        Room.databaseBuilder(context.applicationContext, PostDB::class.java, "post.db").build()
            }
            return sInstance!!
        }
    }
}