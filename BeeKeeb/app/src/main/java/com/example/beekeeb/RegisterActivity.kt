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
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack22_1.BeeKeeb.util.Util

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val storage = FirebaseStorage.getInstance()
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
            val array = listOf("ihbuj")

            val errors = arrayOf<Int>(
                R.string.fields_error,
                R.string.register_email_error,
                R.string.database_error,
                R.string.credential_invalid
            )

            if (email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Please fill all of the required fields", Toast.LENGTH_SHORT).show()
            }else if (email.length <= 10 || (!email.endsWith("@gmail.com") && !email.endsWith("@yahoo.com"))){
                Toast.makeText(this, errors[1], Toast.LENGTH_SHORT).show()
            }
            else{
                val storagePath = "defaultPicture/defaultPicture.jpg"
                storage.reference.child(storagePath).downloadUrl.addOnSuccessListener { url ->
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
                                "user_profile_picture" to url.toString(),
                                "user_about" to "",
                                "following" to array,
                                "followedBy" to array
                            )
                            db.collection("users").document(uid).set(data)
                                .addOnSuccessListener {
                                    this.finish()
                                    val intent = Intent(this, LoginEmailActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener{
                                        e-> Toast.makeText(this, errors[2], Toast.LENGTH_SHORT).show()
                                }

                        }else{
                            Toast.makeText(this, errors[3], Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, errors[2], Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Log.d("error", "error while getting download url ")
                }

            }
        }
    }
}