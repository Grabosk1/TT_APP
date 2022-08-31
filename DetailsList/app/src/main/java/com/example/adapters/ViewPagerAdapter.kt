package com.example.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fragments.HomeFragment
import com.example.fragments.MainBiggerFragment
import com.example.fragments.MainFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }
            1 -> MainFragment()
            else -> MainBiggerFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}

