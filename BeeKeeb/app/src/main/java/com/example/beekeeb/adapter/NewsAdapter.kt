package com.example.beekeeb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.R
import com.example.beekeeb.model.News
import com.example.beekeeb.model.Post

class NewsAdapter(private val newsList: ArrayList<News>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var onItemClicked: ((News) -> Unit)? = null

    class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImageView: ImageView = itemView.findViewById(R.id.profileIV)
        val newsTextView: TextView = itemView.findViewById(R.id.newsTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return NewsViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.newsTextView.text = news.news
//        set image

        holder.itemView.setOnClickListener{
            onItemClicked?.invoke(news)
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }


}