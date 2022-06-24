package com.usc0der.ydprojectnew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.usc0der.ydprojectnew.R
import com.usc0der.ydprojectnew.adapter.ViewPagerAdapter
import com.usc0der.ydprojectnew.api.viewmodel.ShareViewModel
import com.usc0der.ydprojectnew.databinding.FragmentMediaBinding
import com.usc0der.ydprojectnew.utils.SharePref


class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharePref: SharePref
    private val shareViewModel: ShareViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()
        sharePref = SharePref(requireContext())
    }

    fun initTab() {
        val iconList = intArrayOf(R.drawable.ic_youtube, R.drawable.ic_headphones)
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = arrayListOf("Videolar", "Audiolar")[position]
            tab.setIcon(iconList[0])
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}