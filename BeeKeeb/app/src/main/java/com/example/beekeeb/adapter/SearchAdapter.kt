package com.example.beekeeb.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.bluejack22_1.BeeKeeb.frag.PostsearchFragment
import edu.bluejack22_1.BeeKeeb.frag.UsersearchFragment

class SearchAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifeCycle){

    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return PostsearchFragment()
            1 -> return UsersearchFragment()
            else -> return  PostsearchFragment()
        }
    }

}