package com.example.beekeeb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import com.example.beekeeb.adapter.SearchAdapter
import com.example.beekeeb.databinding.ActivitySearchBinding
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.tabs.TabLayoutMediator

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
//    var user = getString(R.string.user)
//    var post = getString(R.string.post)
//    var tableTitle = arrayOf(user, post)

    var tableTitle = arrayOf("Post", "User")

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySearchBinding.inflate(layoutInflater)

        var pager = binding.contentVP
        var tabLayout = binding.searchTL
        pager.adapter = SearchAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, pager){
            tab, position ->
                tab.text = when(position){
                    0 -> getString(R.string.post)
                    else -> getString(R.string.user)
                }
        }.attach()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}