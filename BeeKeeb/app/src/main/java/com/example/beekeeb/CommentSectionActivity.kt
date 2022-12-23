package com.example.beekeeb

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityCommentSectionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CommentSectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentSectionBinding
    private lateinit var postUID: String
    private lateinit var db: FirebaseFirestore
    private lateinit var currUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCommentSectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        currUser = FirebaseAuth.getInstance().currentUser!!
        postUID = intent.getStringExtra("postUID").toString()
        db = FirebaseFirestore.getInstance()
        var error = getString(R.string.invalidQ)
        var success = getString(R.string.successQ)
        var dbError = getString(R.string.dbError)

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