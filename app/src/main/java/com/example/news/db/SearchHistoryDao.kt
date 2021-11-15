package com.example.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.model.SearchHistory

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM searchhistory")
    fun getAll(): List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg history: SearchHistory)

    @Query("DELETE FROM searchhistory")
    fun delete()
}