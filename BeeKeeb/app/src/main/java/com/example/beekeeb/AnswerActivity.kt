package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beekeeb.adapter.AnswerAdapter
import com.example.beekeeb.databinding.ActivityAnswerBinding
import com.example.beekeeb.model.Answer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnswerBinding
    private lateinit var postUID: String
    private lateinit var db: FirebaseFirestore
    private lateinit var currUser: FirebaseUser

    private lateinit var questionUID: String

    private lateinit var answerList: ArrayList<Answer>
    private lateinit var adapterAnswer: AnswerAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAnswerBinding.inflate(layoutInflater)
        currUser = FirebaseAuth.getInstance().currentUser!!
        postUID = intent.getStringExtra("postUID").toString()
        questionUID = intent.getStringExtra("questionUID").toString()
        db = FirebaseFirestore.getInstance()

        val questionRef = db.collection("posts/"+postUID+"/questions").document(questionUID)
        val answerRef = db.collection("posts/"+postUID+"/questions/"+questionUID+"/answers")

        recyclerView = binding.answerRV
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        answerList = ArrayList()
        answerList.clear()
        questionRef.get().addOnSuccessListener { doc ->
            if (doc != null){
                val question = doc.data?.get("question").toString()

                val sender = doc.data?.get("sender").toString()

                binding.questionTV.text = question

                val senderRef = db.collection("users").document(sender)

                senderRef.get().addOnSuccessListener { doc ->
                    if(doc != null){
                        val username = doc.data?.get("user_name").toString()
                        val profile_picture = doc.data?.get("user_profile_picture").toString()
                        binding.authorTV.text = username
                        Picasso.get().load(profile_picture).fit().centerCrop().into(binding.profileIV)
                    }
                }
            }
        }

        answerRef.addSnapshotListener{ value, e ->
            if (e != null){
                return@addSnapshotListener
            }
            val answers = ArrayList<Answer>()
            for(doc in value!!){
                val sender = doc.data.get("sender").toString()
                val answer = doc.data.get("answer").toString()
                val answerID = doc.id
                val userRef = db.collection("users").document(sender)
                userRef.get().addOnSuccessListener { doc ->
                    if(doc != null){
                        val username = doc.data?.get("user_name").toString()
                        val profilePic = doc.data?.get("user_profile_picture").toString()
                        answers.add(Answer(answer, sender, username, profilePic))
                        answerList = answers
                        adapterAnswer = AnswerAdapter(answerList)
                        recyclerView.adapter = adapterAnswer
                    }
                }
            }
        }

        binding.sendBtn.setOnClickListener{
            val error = R.string.invalidAns
            val success = R.string.successAns
            val answer = binding.etAnswer.text.toString()

            if (!answer.isEmpty()){
                val answer = hashMapOf(
                    "sender" to currUser.uid.toString(),
                    "answer" to answer
                )
//                val path = "posts/"+postUID+"/questions"
                val path = "posts/"+postUID+"/questions/"+questionUID+"/answers"
                db.collection(path).add(answer).addOnSuccessListener {
                    Toast.makeText(this, success, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }


        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}