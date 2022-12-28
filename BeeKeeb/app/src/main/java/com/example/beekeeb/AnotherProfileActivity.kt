package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.adapter.postAdapter
import com.example.beekeeb.databinding.ActivityAnotherProfileBinding
import com.example.beekeeb.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldValue.arrayRemove
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.FirebaseFirestore

class AnotherProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnotherProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var profileUID: String
    private lateinit var curruser: FirebaseUser

    private lateinit var postData: ArrayList<Post>
    private lateinit var adapterPost: postAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var following: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAnotherProfileBinding.inflate(layoutInflater)
        db = FirebaseFirestore.getInstance()
        profileUID = intent.getStringExtra("profileUID").toString()
        curruser = FirebaseAuth.getInstance().currentUser!!

        recyclerView = binding.postRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postData = ArrayList()

        val path = db.collection("users").document(curruser.uid)
        val pathOther = db.collection("users").document(profileUID)

        path.addSnapshotListener{ value, e ->
            if(e != null){
                return@addSnapshotListener
            }
            if(value != null && value.exists()){
                following = value.data?.get("following") as List<String>

                if (following.contains(profileUID)){
                    binding.followBtn.visibility = View.GONE
                    binding.unfollowBtn.visibility = View.VISIBLE
                }else{
                    binding.followBtn.visibility = View.VISIBLE
                    binding.unfollowBtn.visibility = View.GONE
                }
            }
        }



        binding.followBtn.setOnClickListener{
            Log.d("debuk", "satu")
            val list = listOf(profileUID)
            Log.d("debuk", "dua")
            val update = mapOf("following" to arrayUnion(profileUID))
            val currUpdate = mapOf("followedBy" to arrayUnion(curruser.uid))
            Log.d("debuk", "tiga")

            val currUserPath = db.collection("users").document(profileUID)
            currUserPath.update(currUpdate).addOnSuccessListener {
                Log.d("errorhehe", "success")
                Log.d("debuk", "success")
            }.addOnFailureListener{
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                Log.d("errorhehe", it.message.toString())
            }

            val userPath = db.collection("users").document(curruser.uid)
            Log.d("debuk", curruser.uid)
            Log.d("debuk", "empat")
            userPath.update(update).addOnSuccessListener {
                Log.d("errorhehe", "success")
                Log.d("debuk", "success")
            }.addOnFailureListener{
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                Log.d("errorhehe", it.message.toString())
            }
            Log.d("debuk", "lima")
        }

        binding.unfollowBtn.setOnClickListener{
            val list = listOf(profileUID)
            val update = mapOf("following" to arrayRemove(profileUID))
            val userPath = db.collection("users").document(curruser.uid)
            userPath.update(update)
        }




        pathOther.get().addOnSuccessListener { doc ->
            val username = doc.get("user_name").toString()
            val profilePicture = doc.get("user_profile_picture").toString()

            val currUsePath = db.collection("users").document(curruser.uid)
            currUsePath.get().addOnSuccessListener { data ->
                following = data.get("following") as List<String>

                if (following.contains(profileUID)){
                    binding.followBtn.visibility = View.GONE
                    binding.unfollowBtn.visibility = View.VISIBLE
                }else{
                    binding.followBtn.visibility = View.VISIBLE
                    binding.unfollowBtn.visibility = View.GONE
                }
            }

            binding.usernameTv.text = username
//            set profile picture nya

            val postRef = db.collection("posts").orderBy("author").startAt(profileUID).endAt(profileUID+"\uf8ff")
            val docs = postRef.get()
            docs.addOnSuccessListener { documents ->
                for (document in documents){
                    val title = document.get("title").toString();
                    val thread = document.get("thread").toString();
                    val path = document.get("path").toString();
                    val like = document.get("like").toString().toInt();
                    val tag = document.get("tag").toString();
                    val author = document.get("author").toString();
                    val uid = document.get("uid").toString()

                    postData.add(Post(title, thread, tag, path, username, profileUID, profilePicture, like, uid))
                    adapterPost = postAdapter(postData)
                    recyclerView.adapter = adapterPost
                    adapterPost.onItemClicked = {
                        val intent = Intent(this, PostDetailActivity::class.java)
                        intent.putExtra("uid", it.uid)
                        intent.putExtra("authorUID", profileUID)
                        startActivity(intent)
                    }
                }
            }

        }


        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}