package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.beekeeb.databinding.ActivityMainPageBinding
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_1.BeeKeeb.frag.*

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(HomeFragment())
        auth = FirebaseAuth.getInstance()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> changeFragment(HomeFragment())
                R.id.miAdd -> changeFragment(AddFragment())
                R.id.miNews -> changeFragment(NewsFragment())
                R.id.miProfile -> changeFragment(ProfileUserFragment())

                else ->{

                }


            }

            true
        }
    }

    private fun changeFragment(frag : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, frag)
        fragmentTransaction.commit()
    }

}