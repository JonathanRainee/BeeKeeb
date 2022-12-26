package com.example.beekeeb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.R
import com.example.beekeeb.model.Question
import com.squareup.picasso.Picasso

class questionAdapter (private val questionList: ArrayList<Question>): RecyclerView.Adapter<questionAdapter.QuestionViewHolder>() {

    var onItemClicked: ((Question)->Unit)? = null

    class QuestionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImageView: ImageView = itemView.findViewById(R.id.profileIV)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTV)
        val questionTextView: TextView = itemView.findViewById(R.id.questionTV)
        val replyImageView: ImageView = itemView.findViewById(R.id.replyIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.comment_card, parent, false)
        return QuestionViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.questionTextView.text = question.question
        holder.authorTextView.text = question.authorName
        Picasso.get().load(question.authorPicture).fit().centerCrop().into(holder.profileImageView)

        holder.replyImageView.setOnClickListener{
            onItemClicked?.invoke(question)
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }



}