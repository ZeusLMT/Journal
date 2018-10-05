package com.wanderer.journal.dataStorage

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [(ForeignKey(entity = Post::class, parentColumns = ["time"], childColumns = ["timestamp"]))])
data class Location (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val timestamp: String,
        val latitude: String,
        val longitude: String,
        val neighbourhood: String,
        val city: String,
        val country: String) {


    override fun toString(): String {
        return "$neighbourhood, $city, $country"
    }
}
