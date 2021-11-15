package com.example.news.api

import com.example.news.model.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v2/top-headlines")
    fun getTotalNews(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<ResponseData?>?

    @GET("v2/top-headlines")
    fun getNewsWithSource(
        @Query("q") sources: String?,
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<ResponseData?>?

}