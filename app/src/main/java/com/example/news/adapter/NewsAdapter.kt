package com.example.news.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.listeners.onNewsClickLisener
import com.example.news.model.News
import com.example.news.utils.Util

class NewsAdapter( private val context: Context,private val onNewsClickLisener: onNewsClickLisener): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var items: ArrayList<News> = arrayListOf()

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val news = itemView.findViewById<LinearLayout>(R.id.news)
        val description = itemView.findViewById<TextView>(R.id.description)
        val source = itemView.findViewById<TextView>(R.id.source)
        val date = itemView.findViewById<TextView>(R.id.date)
        val image = itemView.findViewById<ImageView>(R.id.image)
        val divider = itemView.findViewById<View>(R.id.divider)
    }

    fun updateData(items: ArrayList<News>){
        Log.d(TAG,"")
        this.items = arrayListOf()
        this.items = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        Log.d(TAG,"")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_single, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder")
        holder.title.text = items[position].title
        holder.description.text = items[position].description
        holder.source.text = context.resources.getString(R.string.source,items[position].source.name)
        holder.date.text = Util.formatDates(items[position].publishedAt)
        holder.news.setOnClickListener {onNewsClickLisener.onSelectNews(items[position])}
        Glide.with(holder.itemView).load(items[position].urlToImage).into(holder.image)
        if(position==items.size-1)
            holder.divider.visibility = View.GONE
    }

    override fun getItemCount()  : Int {
        return if(items.isNullOrEmpty()){
            Log.d(TAG,"null")
            0
        }
        else
            items.size
    }

    companion object{
        private const val TAG= "NewsAdapter"
    }
}