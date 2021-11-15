package com.example.news.di.module

import android.app.Application
import androidx.room.Room
import com.example.news.db.AppDatabase
import com.example.news.db.SearchHistoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RoomModule(private val application: Application) {
    private val appDatabase: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "database-name"
    ).build()

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        return appDatabase
    }

    @Singleton
    @Provides
    fun providesSearchHistoryDao(appDatabase: AppDatabase): SearchHistoryDao {
        return appDatabase.searchHistoryDao()
    }

}