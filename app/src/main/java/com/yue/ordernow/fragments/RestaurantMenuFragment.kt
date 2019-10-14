package com.yue.ordernow.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.yue.ordernow.R
import com.yue.ordernow.adapters.MenuPageViewAdapter
import com.yue.ordernow.databinding.FragmentRestaurantMenuBinding

class RestaurantMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRestaurantMenuBinding.inflate(inflater, container, false)
        binding.menuPage.adapter = MenuPageViewAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.menuPage) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        return binding.root
    }

    private fun getTabTitle(position: Int): String? = when (position) {
        0 -> getString(R.string.appetizer_title)
        1 -> getString(R.string.breakfast_title)
        2 -> getString(R.string.main_title)
        3 -> getString(R.string.drink_title)
        else -> null
    }
}
