package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityRegisterBinding
import com.example.beekeeb.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()
            val username = binding.etFirstName.text.toString() + " " + binding.etLastName.text.toString()
            val phoneNum = binding.etPhone.text.toString()

            if (email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Please fill all of the required fields", Toast.LENGTH_SHORT).show()
            }else if (!email.endsWith("@gmail.com")){
                Toast.makeText(this, "Email must ends with @gmail.com", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if (it.isSuccessful){
                        val uid = firebaseAuth.currentUser?.uid.toString()
                        val data = hashMapOf(
                            "user_id" to uid,
                            "user_email" to email,
                            "user_password" to pass,
                            "user_name" to username,
                            "user_phone" to phoneNum,
                            "user_birthdate" to "",
                            "user_profile_picture" to "",
                            "user_about" to ""
                        )
                        db.collection("users").document(uid).set(data)
                            .addOnSuccessListener {
                                this.finish()
                                val intent = Intent(this, LoginEmailActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener{
                                    e-> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                            }

                    }else{
                        Toast.makeText(this, "There's an error in our database, try again later", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}