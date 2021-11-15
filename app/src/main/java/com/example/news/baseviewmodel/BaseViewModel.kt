package com.example.news.baseviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.api.Api
import com.example.news.api.ApiInterface
import com.example.news.db.AppDatabase
import com.example.news.model.News
import com.example.news.model.Response
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

    private fun getAllNews() {
        val result = client.getTotalNews("us", API_KEY)
        result?.enqueue(object : Callback<Response?> {
            override fun onResponse(
                call: Call<Response?>,
                response: retrofit2.Response<Response?>
            ) {
                Log.d(TAG, "")
                newsLiveData.postValue(response.body()?.articles)
            }

            override fun onFailure(call: Call<Response?>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                newsLiveData.postValue(null)
            }

        })
    }

    fun fillSearchHistory(db: AppDatabase) {
        Log.d(TAG, "")
        val thread = Thread {
            val list = db.searchHistoryDao().getAll().map { it.source }
            searchHistory.postValue(list)
        }
        thread.start()
    }

    fun getNewsWithSource(query: String, db: AppDatabase) {
        val result = client.getNewsWithSource(query, API_KEY)
        result?.enqueue(object : Callback<Response?> {
            override fun onResponse(
                call: Call<Response?>,
                response: retrofit2.Response<Response?>
            ) {
                Log.d(TAG, "")
                val list = response.body()?.articles
                if (!list.isNullOrEmpty()) {
                    newsLiveData.postValue(list)
                    val thread = Thread {
                        db.searchHistoryDao().insertAll(SearchHistory(query))
                    }
                    thread.start()
                }
            }

            override fun onFailure(call: Call<Response?>, t: Throwable) {
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