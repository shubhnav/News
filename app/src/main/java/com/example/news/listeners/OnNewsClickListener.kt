package com.example.news.listeners

import com.example.news.model.News

interface OnNewsClickListener {
    fun onSelectNews(news: News)
}