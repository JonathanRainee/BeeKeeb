package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

//        binding.btnSignIn.setOnClickListener{
//            val intent = Intent(this, LoginEmailActivity::class.java)
//            startActivity(intent)
//        }

        binding.btnSignUp.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()

            if (email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Please fill all of the required fields", Toast.LENGTH_SHORT).show()
            }else if (!email.endsWith("@gmail.com")){
                Toast.makeText(this, "Email must ends with @gmail.com", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, LoginEmailActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "There's an error in our database, try again later", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}