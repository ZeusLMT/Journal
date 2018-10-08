package com.wanderer.journal.dataStorage

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity
data class Post(
    @PrimaryKey
    val time: String,
    val image: String,
    val description: String,
    val weather: String,
    @Embedded
    val location: Location): Comparable<Post> {
    override fun compareTo(other: Post): Int {
        val spd = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
        val thisTime = spd.parse(this.time)
        val otherTime = spd.parse(other.time)

        return otherTime.compareTo(thisTime)
    }

    override fun toString(): String = "$time $image $description $location $weather"
}
