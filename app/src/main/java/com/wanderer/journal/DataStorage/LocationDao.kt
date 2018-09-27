package com.wanderer.journal.DataStorage

import android.arch.persistence.room.*

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location)

    @Update
    fun update(location: Location)

    @Delete
    fun deletePost(location: Location)
}