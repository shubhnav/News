package com.example.news.activity

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R

class DetailedNews : AppCompatActivity() {

    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"")
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(NEWS_URL)
        setContentView(R.layout.detailed_news)
        webview = findViewById<WebView>(R.id.webview)
        if (url != null) {
            webview.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        Log.d(TAG,"")
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val NEWS_URL= "news_url"
        private const val TAG= "DetailedNews"
    }
}