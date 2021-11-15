package com.example.news.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.model.SearchHistory

@Database(entities = [SearchHistory::class], version = 1)
abstract class
AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}