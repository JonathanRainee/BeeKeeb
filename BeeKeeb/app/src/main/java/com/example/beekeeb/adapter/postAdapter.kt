package com.example.beekeeb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.R
import com.example.beekeeb.databinding.PostCardBinding
import com.example.beekeeb.model.Post
import com.squareup.picasso.Picasso

class postAdapter (private val postList: ArrayList<Post>): RecyclerView.Adapter<postAdapter.PostViewHolder>() {

    private lateinit var binding: PostCardBinding

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImageView: ImageView = itemView.findViewById(R.id.profileIV)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTV)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTV)
        val threadTextView: TextView = itemView.findViewById(R.id.threadTV)
        val mediaImageView: ImageView = itemView.findViewById(R.id.mediaIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
//        binding = PostCardBinding.inflate(inflater, parent, false)
        return PostViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.authorTextView.text = post.authorName
        Picasso.get().load(post.authorPicture).fit().centerCrop().into(holder.profileImageView)
        holder.titleTextView.text = post.title
        holder.threadTextView.text = post.thread
        Picasso.get().load(post.path).fit().centerCrop().into(holder.mediaImageView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

}