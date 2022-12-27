package com.example.beekeeb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.R
import com.example.beekeeb.model.Post
import com.example.beekeeb.model.User

class ProfileAdapter(private val profileList: ArrayList<User>): RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    var onItemClicked: ((User) -> Unit)? = null

    class ProfileViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImageView: ImageView = itemView.findViewById(R.id.profileIV)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTV)
        val aboutTextView: TextView = itemView.findViewById(R.id.aboutTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.profile_card, parent, false)
        return ProfileViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profileList[position]
        holder.authorTextView.text = profile.username
        holder.aboutTextView.text = profile.about

        holder.itemView.setOnClickListener{
            onItemClicked?.invoke(profile)
        }
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

}