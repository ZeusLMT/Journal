package com.wanderer.journal.DataStorage

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap

@Entity
data class Post(
    @PrimaryKey
    val time: String,
    val image: String,
    val description: String,
    val location: String){
    override fun toString(): String = "$time $image $description $location"
}
