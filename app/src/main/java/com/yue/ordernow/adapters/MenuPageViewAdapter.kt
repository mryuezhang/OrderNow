package com.yue.ordernow.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yue.ordernow.fragments.MenuOptionsFragment
import com.yue.ordernow.utilities.APPETIZER
import com.yue.ordernow.utilities.BREAKFAST
import com.yue.ordernow.utilities.DRINK
import com.yue.ordernow.utilities.MAIN


const val APPETIZER_PAGE_INDEX = 0
const val BREAKFAST_PAGE_INDEX = 1
const val MAIN_PAGE_INDEX = 2
const val DRINK_PAGE_INDEX = 3

class MenuPageViewAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val fragments: Map<Int, () -> Fragment> = mapOf(
        APPETIZER_PAGE_INDEX to { MenuOptionsFragment.newInstance(APPETIZER) },
        BREAKFAST_PAGE_INDEX to { MenuOptionsFragment.newInstance(BREAKFAST) },
        MAIN_PAGE_INDEX to { MenuOptionsFragment.newInstance(MAIN) },
        DRINK_PAGE_INDEX to { MenuOptionsFragment.newInstance(DRINK) }
    )

    override fun createFragment(position: Int): Fragment =
        fragments[position]?.invoke() ?: throw IndexOutOfBoundsException()

    override fun getItemCount(): Int = fragments.size
}