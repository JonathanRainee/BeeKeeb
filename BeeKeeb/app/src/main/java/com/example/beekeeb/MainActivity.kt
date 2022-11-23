package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout);

        val signInBtn = findViewById<Button>(R.id.btnSignIn)
        val signUpBtn = findViewById<Button>(R.id.btnSignUp)

        signInBtn.setOnClickListener(){
            val intentSignIn = Intent(this@MainActivity, LoginOption_activity::class.java)
            startActivity(intentSignIn)
        }

        signUpBtn.setOnClickListener(){
            val intentSignUp = Intent(this@MainActivity, Register_activity::class.java)
            startActivity(intentSignUp)
        }
    }
}