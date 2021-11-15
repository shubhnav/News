package com.example.news.baseviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.api.Api
import com.example.news.api.ApiInterface
import com.example.news.db.SearchHistoryDao
import com.example.news.model.News
import com.example.news.model.ResponseData
import com.example.news.model.SearchHistory
import retrofit2.Call
import retrofit2.Callback


class BaseViewModel : ViewModel() {

    var newsLiveData: MutableLiveData<List<News>> = MutableLiveData()
    private val client = Api.retrofit.create(ApiInterface::class.java)
    var searchHistory: MutableLiveData<List<String>> = MutableLiveData()

    init {
        getAllNews()
    }

    fun getAllNews() {
        val result = client.getTotalNews("us", API_KEY)
        result?.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(
                call: Call<ResponseData?>,
                responseData: retrofit2.Response<ResponseData?>
            ) {
                Log.d(TAG, "")
                newsLiveData.postValue(responseData.body()?.articles)
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                newsLiveData.postValue(null)
            }
        })
    }

    fun fillSearchHistory(searchHistoryDao: SearchHistoryDao) {
        Log.d(TAG, "")
        val thread = Thread {
            val list = searchHistoryDao.getAll().map { it.source }
            searchHistory.postValue(list)
        }
        thread.start()
    }

    fun getNewsWithSource(query: String, searchHistoryDao: SearchHistoryDao) {
        val result = client.getNewsWithSource(query, "us", API_KEY)
        result?.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(
                call: Call<ResponseData?>,
                responseData: retrofit2.Response<ResponseData?>
            ) {
                Log.d(TAG, "")
                val list = responseData.body()?.articles
                if (!list.isNullOrEmpty()) {
                    newsLiveData.postValue(list)
                    val thread = Thread {
                        searchHistoryDao.insertAll(SearchHistory(query))
                    }
                    thread.start()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.e(TAG, "${t.message}")
                newsLiveData.postValue(null)
            }

        })
    }

    companion object {
        private const val TAG = "BaseViewModel"
        private const val API_KEY = "50c1bd99f6464242aab6405aa2ca35a5"
    }

}