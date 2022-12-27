package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import com.example.beekeeb.adapter.SearchAdapter
import com.example.beekeeb.databinding.ActivitySearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    var tableTitle = arrayOf(R.string.user, R.string.post)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySearchBinding.inflate(layoutInflater)

        var pager = binding.contentVP
        var tabLayout = binding.searchTL
        pager.adapter = SearchAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, pager){
            tab, position ->
                tab.text = tableTitle[position].toString()
        }.attach()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}