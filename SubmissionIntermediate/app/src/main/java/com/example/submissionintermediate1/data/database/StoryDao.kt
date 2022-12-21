package com.example.submissionintermediate1.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg stories: StoryResponseItem)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, StoryResponseItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}