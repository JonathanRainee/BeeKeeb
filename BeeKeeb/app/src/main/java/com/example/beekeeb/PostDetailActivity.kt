package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.beekeeb.databinding.ActivityPostDetailBinding
import com.example.beekeeb.model.CreatePost
import com.example.beekeeb.model.User
import com.example.beekeeb.queries.QueriesPost
import com.example.beekeeb.queries.QueriesUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var postUID: String
    private lateinit var Author: User
    private lateinit var Post: CreatePost
    private lateinit var AuthorUID: String
    private lateinit var title: String
    private lateinit var thread: String
    private lateinit var tag: String
    private lateinit var path: String
    private lateinit var authorID: String
    private lateinit var like: String
    private lateinit var uid: String

    private lateinit var AuthorName: String
    private lateinit var AuthorProfPic: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        var currUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        postUID = intent.getStringExtra("uid").toString()
//        Post = getPost(postUID).add
        AuthorUID = intent.getStringExtra("authorUID").toString()
//        Author = QueriesUser.getPostAuthor(AuthorUID)

        val db = Firebase.firestore
        var post = CreatePost("", "", "", "", "", 0, "")
        val userDocRef = db.collection("users").document(AuthorUID)
        userDocRef.get().addOnSuccessListener { doc ->
            if(doc != null){
                AuthorName = doc.data?.get("user_name").toString()
//                AuthorProfPic = doc.data?.get("user_profile_picture").toString()
                binding.authorTV.setText(AuthorName)
            }
        }

        val docref = db.collection("posts").document(postUID)
        docref.get().addOnSuccessListener { doc ->
            if(doc != null){
                title = doc.data?.get("title").toString()
                thread = doc.data?.get("thread").toString()
                tag = doc.data?.get("tag").toString()
                path = doc.data?.get("path").toString()
                authorID = doc.data?.get("author").toString()
                like = doc.data?.get("like").toString()
                uid = doc.data?.get("uid").toString()
                Log.d("query", title+" "+thread+" "+tag+" "+path+" "+authorID+" "+like+" "+uid)
                post = CreatePost(title, thread, tag, path, authorID, like.toInt(), uid)
                binding.titleTV.setText(title)
                binding.threadTV.setText(thread)
            }
        }


//        binding.profileIV = set dari Author (line 26) punya prof img
//        binding.mediaIV = set dati Post (line 24) punya media

        if(AuthorUID != currUserID){
            binding.deleteIV.visibility = View.GONE
        }

        binding.qnaIV.setOnClickListener{
            val intent = Intent(this, CommentSectionActivity::class.java)
            startActivity(intent)
        }
//
        binding.deleteIV.setOnClickListener {
            QueriesPost.deletePost(postUID, this)
            this.finish()
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)
    }
}