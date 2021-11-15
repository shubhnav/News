package com.example.news.model

data class Response(val status:String, val totalResults: Int, val articles: List<News>)
