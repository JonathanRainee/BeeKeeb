package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.beekeeb.databinding.ActivityResetpasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetpasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetpasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetpasswordBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.btnSend.setOnClickListener{
            val email = binding.etEmail.text.toString()

            val failMsg = R.string.database_error
            val emailSent = R.string.sent_email_reset_pass_message

            if(email.isEmpty() || !email.endsWith("@gmail.com")){
                Toast.makeText(this, "Please input a valid email", Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "A reset password email has been sent to your email", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, failMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}