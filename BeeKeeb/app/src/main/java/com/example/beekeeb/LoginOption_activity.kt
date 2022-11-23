package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginOption_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginoption_layout)

        val emailButton = findViewById<Button>(R.id.btnEmail)

        emailButton.setOnClickListener(){
            val intentEmail = Intent(this@LoginOption_activity, loginEmail_activity::class.java)
            startActivity(intentEmail)
        }
    }
}