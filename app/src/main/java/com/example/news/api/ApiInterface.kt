package com.example.news.api

import com.example.news.model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v2/top-headlines")
    fun getTotalNews(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<Response?>?

    @GET("v2/top-headlines")
    fun getNewsWithSource(
        @Query("sources") sources: String?,
        @Query("apiKey") apiKey: String?
    ): Call<Response?>?

}