package com.example.news.di.component

import android.app.Application
import com.example.news.MainActivity
import com.example.news.db.AppDatabase
import com.example.news.db.SearchHistoryDao
import com.example.news.di.module.AppModule
import com.example.news.di.module.RoomModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun getAppDatabase(): AppDatabase
    fun getSearchHistoryDao(): SearchHistoryDao
    fun application(): Application
}