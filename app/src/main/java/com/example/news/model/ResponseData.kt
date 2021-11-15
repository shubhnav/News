package com.example.news.model

data class ResponseData(val status: String, val totalResults: Int, val articles: List<News>)
