package com.example.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.activity.DetailedNews
import com.example.news.activity.DetailedNews.Companion.NEWS_URL
import com.example.news.adapter.NewsAdapter
import com.example.news.baseviewmodel.BaseViewModel
import com.example.news.db.AppDatabase
import com.example.news.db.DbInstance
import com.example.news.listeners.onNewsClickLisener
import com.example.news.model.News

class MainActivity : AppCompatActivity(), onNewsClickLisener, PopupMenu.OnMenuItemClickListener{

    private lateinit var viewModel: BaseViewModel
    private lateinit var rv: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var searchView: SearchView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        setAdapter()
        viewModel=  ViewModelProvider(this).get(BaseViewModel::class.java)
        viewModel.newsLiveData.observe(this, {
                it ->
            Log.d(TAG, "$it")
            if(!it.isNullOrEmpty())
                adapter.updateData(ArrayList(it))
        })
        db = DbInstance.getInstance(applicationContext)
        viewModel.fillSearchHistory(db)
        setSearchView()
    }

    private fun setAdapter(){
        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(this,this)
        rv.adapter = adapter
    }

    private fun setSearchView(){
        searchView = findViewById(R.id.search_src_text)
        searchView.setOnSearchClickListener { v->
            showPopup(v)
        }

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.getNewsWithSource(query,db)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    viewModel.getNewsWithSource(newText,db)
                }
                return true
            }
        })
    }

    override fun onSelectNews(news: News) {
        Log.d(TAG, "$news")
        val intent = Intent()
        intent.setClass(this, DetailedNews::class.java)
        intent.putExtra(NEWS_URL, news.url)
        startActivity(intent)
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        viewModel.searchHistory.observe(this,{
            it.forEach { item->
                popup.menu.add(item)
            }
        })
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        searchView.setQuery(item?.title,true)
        return true
    }

    companion object{
        private const val TAG= "MainActivity"
    }
}
