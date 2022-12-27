package com.example.beekeeb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.R
import com.example.beekeeb.model.Answer
import com.squareup.picasso.Picasso

class AnswerAdapter (private val answerList: ArrayList<Answer>): RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>(){

    class AnswerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImageView: ImageView = itemView.findViewById(R.id.profileIV)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTV)
        val answerTextView: TextView = itemView.findViewById(R.id.answerTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.answer_card, parent, false)
        return AnswerViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answerList[position]
        holder.authorTextView.text = answer.authorName
        holder.answerTextView.text = answer.answer
        Picasso.get().load(answer.authorPicture).fit().centerCrop().into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        return  answerList.size
    }
}