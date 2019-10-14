package com.yue.ordernow.fragments


import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yue.ordernow.R
import com.yue.ordernow.utils.APPETIZER
import com.yue.ordernow.utils.BREAKFAST
import com.yue.ordernow.utils.DRINK
import com.yue.ordernow.utils.MAIN
import kotlinx.android.synthetic.main.fragment_restaurant_menu.*

class RestaurantMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_restaurant_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val menuPageViewAdapter = MenuPageViewAdapter(childFragmentManager)
        menu_page.adapter = menuPageViewAdapter
        tab_layout.setupWithViewPager(menu_page)
    }

    private class MenuPageViewAdapter(
        fm: FragmentManager
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var registeredFragments = SparseArray<Fragment>()

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> MenuOptionsFragment.newInstance(APPETIZER)
            1 -> MenuOptionsFragment.newInstance(BREAKFAST)
            2 -> MenuOptionsFragment.newInstance(MAIN)
            else -> MenuOptionsFragment.newInstance(DRINK)
        }

        override fun getCount(): Int = 4

        override fun getPageTitle(position: Int): CharSequence = when (position) {
            0 -> APPETIZER
            1 -> BREAKFAST
            2 -> MAIN
            else -> DRINK
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }
}
