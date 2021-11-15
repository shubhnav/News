package com.example.news.db

import android.content.Context
import androidx.room.Room

object DbInstance {

    fun getInstance(applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
}