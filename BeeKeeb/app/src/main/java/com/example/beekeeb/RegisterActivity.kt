package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signUpBtn: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        signUpBtn = findViewById<Button>(R.id.btnSignUp)
        db = FirebaseFirestore.getInstance()

        fun addUserToDB(hashMap: HashMap<String, String>){
            db.collection("user").document("test").set(hashMap)
        }

        binding.btnSignUp.setOnClickListener{
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()
            val phoneNumber = binding.etPhone.text.toString()
            val day = binding.etBirthdate.dayOfMonth.toString()
            val month = (binding.etBirthdate.month+1).toString()
            val year = binding.etBirthdate.year.toString()
            val birthdate = day + month + year
            val name = firstName + " " + lastName

            val user = hashMapOf(
                "name" to name,
                "email" to email,
                "phoneNumber" to phoneNumber,
                "birthdate" to birthdate
            )

            if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || phoneNumber.isEmpty()){
                Toast.makeText(this, "Please fill all of the required fields", Toast.LENGTH_SHORT).show()
            }else{
                if(email.endsWith("@gmail.com")){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            addUserToDB(user)
                            val intent = Intent(this, LoginEmailActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }
}