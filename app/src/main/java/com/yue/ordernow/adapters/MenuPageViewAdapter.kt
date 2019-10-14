package com.yue.ordernow.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yue.ordernow.fragments.MenuOptionsFragment
import com.yue.ordernow.utils.APPETIZER
import com.yue.ordernow.utils.BREAKFAST
import com.yue.ordernow.utils.DRINK
import com.yue.ordernow.utils.MAIN


const val APPETIZER_PAGE_INDEX = 0
const val BREAKFAST_PAGE_INDEX = 1
const val MAIN_PAGE_INDEX = 2
const val DRINK_PAGE_INDEX = 3

class MenuPageViewAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MenuOptionsFragment.newInstance(APPETIZER)
        1 -> MenuOptionsFragment.newInstance(BREAKFAST)
        2 -> MenuOptionsFragment.newInstance(MAIN)
        else -> MenuOptionsFragment.newInstance(DRINK)
    }

    override fun getItemCount(): Int = 4
}