package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.beekeeb.databinding.ActivityMainPageBinding
import edu.bluejack22_1.BeeKeeb.frag.AddFragment
import edu.bluejack22_1.BeeKeeb.frag.HomeFragment
import edu.bluejack22_1.BeeKeeb.frag.NewsFragment
import edu.bluejack22_1.BeeKeeb.frag.ProfileFragment

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> changeFragment(HomeFragment())
                R.id.miAdd -> changeFragment(AddFragment())
                R.id.miNews -> changeFragment(NewsFragment())
                R.id.miProfile -> changeFragment(ProfileFragment())

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