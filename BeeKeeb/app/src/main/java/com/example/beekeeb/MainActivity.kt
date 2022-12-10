package com.example.beekeeb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.beekeeb.databinding.ActivityHomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root);


        binding.btnSignIn.setOnClickListener{
            val intentSignIn = Intent(this, LoginOption_activity::class.java)
            startActivity(intentSignIn)
        }

        binding.btnRegister.setOnClickListener{
            Log.d("debugg", "start debug")
            val intentSignIn = Intent(this, LoginOption_activity::class.java)
            val intentRegis = Intent(this, RegisterActivity::class.java)
            val intentRegister = Intent(this, RegisterActivity::class.java)
//            startActivity(intentRegister)
            startActivity(intentRegis)
            Log.d("debugg", "stop debug")
        }
    }
}