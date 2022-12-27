package com.example.beekeeb

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.adapter.postAdapter
import com.example.beekeeb.adapter.questionAdapter
import com.example.beekeeb.databinding.ActivityCommentSectionBinding
import com.example.beekeeb.model.Post
import com.example.beekeeb.model.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CommentSectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentSectionBinding
    private lateinit var postUID: String
    private lateinit var db: FirebaseFirestore
    private lateinit var currUser: FirebaseUser
    private lateinit var questionsList: ArrayList<Question>
    private lateinit var adapterQuestions: questionAdapter
    private lateinit var  recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCommentSectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        currUser = FirebaseAuth.getInstance().currentUser!!
        postUID = intent.getStringExtra("postUID").toString()
        db = FirebaseFirestore.getInstance()
        var error = getString(R.string.invalidQ)
        var success = getString(R.string.successQ)
        var dbError = getString(R.string.dbError)

        recyclerView = binding.questionRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        questionsList = ArrayList()
        questionsList.clear()

        var questionDocRef = db.collection("posts").document(postUID).collection("questions")

        questionDocRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            questionsList.clear()
            for (doc in value!!) {
                val sender = doc.data.get("sender").toString()
                val question = doc.data.get("question").toString()
                val userRef = db.collection("users").document(sender)
                val questionId = doc.id
                userRef.get().addOnSuccessListener { doc ->
                    if (doc != null) {
                        val username = doc.data?.get("user_name").toString()
                        val profilePic = doc.data?.get("user_profile_picture").toString()
                        questionsList.add(Question(question, questionId, username, profilePic))
                        adapterQuestions = questionAdapter(questionsList)
                        recyclerView.adapter = adapterQuestions
                        adapterQuestions.onItemClicked = {
                            val intent = Intent(this, AnswerActivity::class.java)
                            intent.putExtra("postUID", postUID)
                            intent.putExtra("questionUID", it.uid)
                            startActivity(intent)
                        }
                    }
                }
            }
        }




        binding.sendBtn.setOnClickListener{
            var question = binding.etComment.text.toString()

            if (question.isEmpty()){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }else{
                val question = hashMapOf(
                    "sender" to currUser.uid.toString(),
                    "question" to question,
                    "uid" to ""
                )
                val path = "posts/"+postUID+"/questions"
                db.collection(path).add(question).addOnSuccessListener {
                    var id = it.id.toString()
                    db.collection(path).document(id).update(mapOf(
                        "uid" to id
                    ))
                    binding.etComment.setText("")
                    closeKeeb(binding.root)
                    Toast.makeText(this, success, Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, dbError, Toast.LENGTH_SHORT).show()
                }
            }
        }


        setContentView(binding.root)
    }

    fun closeKeeb(view: View){
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}