package com.usc0der.ydprojectnew.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.usc0der.ydprojectnew.ui.AudiosFragment
import com.usc0der.ydprojectnew.ui.VideosFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager,lifecycle) {
    private val list = arrayListOf(VideosFragment(), AudiosFragment())

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {

        return list[position] as Fragment
    }


}