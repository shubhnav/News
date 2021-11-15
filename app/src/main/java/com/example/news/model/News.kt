package com.example.news.model

import java.util.*

data class News(
    val title: String,
    val description: String,
    val source: Source,
    val publishedAt: Date,
    val urlToImage: String,
    val url: String
)
