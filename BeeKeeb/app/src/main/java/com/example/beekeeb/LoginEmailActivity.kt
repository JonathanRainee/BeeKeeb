package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class LoginEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvForgotPass.setOnClickListener{
            val intent = Intent(this, ResetpasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()

            val errors = arrayOf<Int>(
                R.string.fields_error,
                R.string.wrong_credentials_error
            )

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, errors[0], Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        this.finish()
                        val intent = Intent(this, MainPageActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, errors[1], Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, HomepageActivity::class.java)
//            startActivity(intent)
//        }
    }
}