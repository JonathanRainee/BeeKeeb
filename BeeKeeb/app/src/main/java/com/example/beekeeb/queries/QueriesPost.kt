package com.example.beekeeb.queries

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.beekeeb.model.CreatePost
import com.example.beekeeb.model.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.BeeKeeb.util.Util

class QueriesPost {

    companion object{
        fun getPost(postID: String): CreatePost{
            val list = listOf("")
            val db = Firebase.firestore
            var post = CreatePost("", "", "", "", "", 0, "", list)
            val docref = db.collection("posts").document(postID)
            docref.get().addOnSuccessListener { doc ->
                if(doc != null){
                    val title = doc.data?.get("title").toString()
                    val thread = doc.data?.get("thread").toString()
                    val tag = doc.data?.get("tag").toString()
                    val path = doc.data?.get("path").toString()
                    val authorID = doc.data?.get("author").toString()
                    val like = doc.data?.get("like").toString().toInt()
                    val uid = doc.data?.get("uid").toString()
                    val liked = doc.data?.get("likedBy") as List<String>
                    Log.d("query", title+" "+thread+" "+tag+" "+path+" "+authorID+" "+like+" "+uid)
                    post = CreatePost(title, thread, tag, path, authorID, like, uid, liked)
                }
            }
            Log.d("query", post.title+" "+post.thread+" "+post.tag+" "+post.path+" "+post.author+" "+post.like+" "+post.uid)
            return post
        }

        fun deletePost(postID: String, context: Context){
            Util.loadingDialog(context)
            val db = Firebase.firestore
            val docref = db.collection("posts").document(postID).delete()
            docref.addOnSuccessListener {
                Util.dismissLoadingDialog()
                Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Util.dismissLoadingDialog()
                Toast.makeText(context, "Delete failed, please try again later", Toast.LENGTH_SHORT).show()
            }

        }
    }

}