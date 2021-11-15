package com.example.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ProgressBar
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
import com.example.news.db.SearchHistoryDao
import com.example.news.di.component.DaggerApplicationComponent
import com.example.news.di.module.AppModule
import com.example.news.di.module.RoomModule
import com.example.news.listeners.OnNewsClickListener
import com.example.news.model.News
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnNewsClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var viewModel: BaseViewModel
    private lateinit var rv: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var searchView: SearchView
    @Inject
    lateinit var searchHistoryDao: SearchHistoryDao
    private lateinit var progressBar: ProgressBar
    private lateinit var mainDisplay: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        progressBar = findViewById(R.id.progressBar)
        mainDisplay = findViewById(R.id.main_display)
        setSupportActionBar(toolbar)
        val component =
            DaggerApplicationComponent.builder().appModule(AppModule(application))
                .roomModule(RoomModule(application))
                .build()
        component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        setAdapter()
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        viewModel.newsLiveData.observe(this, { it ->
            Log.d(TAG, "$it")
            if (!it.isNullOrEmpty())
                adapter.updateData(ArrayList(it))
            progressBar.visibility = View.GONE
            mainDisplay.visibility = View.VISIBLE
        })
        viewModel.fillSearchHistory(searchHistoryDao)
        setSearchView()
    }

    private fun setAdapter() {
        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(this, this)
        rv.adapter = adapter
    }

    private fun setSearchView() {
        searchView = findViewById(R.id.search_src_text)
        searchView.setOnSearchClickListener { v ->
            showPopup(v)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.getNewsWithSource(query, searchHistoryDao)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                if (!newText.isNullOrEmpty()) {
//                    viewModel.getNewsWithSource(newText,searchHistoryDao)
//                }
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.getAllNews()
            false
        }
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
        viewModel.searchHistory.observe(this, {
            it.forEach { item ->
                popup.menu.add(item)
            }
        })
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        searchView.setQuery(item?.title, true)
        return true
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
