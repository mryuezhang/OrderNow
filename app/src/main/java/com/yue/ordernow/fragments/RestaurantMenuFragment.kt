package com.yue.ordernow.fragments


import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yue.ordernow.R
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.utils.*
import com.yue.ordernow.viewModels.RestaurantMenuViewModel
import kotlinx.android.synthetic.main.fragment_restaurant_menu.*
import java.io.BufferedReader
import java.io.InputStreamReader

class RestaurantMenuFragment : Fragment() {

    private val menuItems = HashMap<Category, ArrayList<MenuItem>>()

    private val viewModel: RestaurantMenuViewModel by viewModels {
        InjectorUtils.provideRestaurantMenuViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_restaurant_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

//        // Either load menu items from previous saved state or file resource
//        if (savedInstanceState == null) {
//            menuItems[Category.Appetizer] = getMenuItemsFromFile(R.raw.appetizers)
//            menuItems[Category.Breakfast] = getMenuItemsFromFile(R.raw.breakfast)
//            menuItems[Category.Maindish] = getMenuItemsFromFile(R.raw.maindishes)
//            menuItems[Category.Drink] = getMenuItemsFromFile(R.raw.drinks)
//        } else {
//            enumValues<Category>().forEach { category ->
//                menuItems[category] = savedInstanceState.getParcelableArrayList(category.name)!!
//            }
//        }

        viewModel.menuItemRepository.getAppetizers().observe(viewLifecycleOwner, Observer {
            menuItems[Category.Appetizer] = it as ArrayList<MenuItem>
        })
        viewModel.menuItemRepository.getBreakfasts().observe(viewLifecycleOwner, Observer {
            menuItems[Category.Breakfast] = it as ArrayList<MenuItem>
        })

        viewModel.menuItemRepository.getMains().observe(viewLifecycleOwner, Observer {
            menuItems[Category.Maindish] = it as ArrayList<MenuItem>
        })

        viewModel.menuItemRepository.getDrinks().observe(viewLifecycleOwner, Observer {
            menuItems[Category.Drink] = it as ArrayList<MenuItem>
        })


        val menuPageViewAdapter = MenuPageViewAdapter(childFragmentManager, menuItems)
        menu_page.adapter = menuPageViewAdapter
        tab_layout.setupWithViewPager(menu_page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        enumValues<Category>().forEach { category ->
            outState.putParcelableArrayList(category.name, menuItems[category])
        }
        super.onSaveInstanceState(outState)
    }

    private fun getMenuItemsFromFile(id: Int): ArrayList<MenuItem> {

        val items = ArrayList<MenuItem>()
        val reader = BufferedReader(InputStreamReader(resources.openRawResource(id)))
        var line = reader.readLine()
        var tokens: List<String>

        while (line != null) {
            tokens = line.split(",")

            try {
                items.add(MenuItem(tokens[0], tokens[1].toFloat(), tokens[2]))
            } catch (e: NumberFormatException) {
                items.add(MenuItem(tokens[0], 0.0F, tokens[2]))
            }

            line = reader.readLine()
        }

        return items
    }

    private class MenuPageViewAdapter(
        fm: FragmentManager,
        var menuItems: HashMap<Category, ArrayList<MenuItem>>
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var registeredFragments = SparseArray<Fragment>()

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> MenuOptionsFragment.newInstance(menuItems[Companion.Category.Appetizer]!!)
            1 -> MenuOptionsFragment.newInstance(menuItems[Companion.Category.Breakfast]!!)
            2 -> MenuOptionsFragment.newInstance(menuItems[Companion.Category.Maindish]!!)
            else -> MenuOptionsFragment.newInstance(menuItems[Companion.Category.Drink]!!)
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

    companion object {

        private const val ITEMS = "items"
        private const val tag = "RestaurantMenuFragment"

        enum class Category {
            Appetizer,
            Breakfast,
            Maindish,
            Drink
        }

        @JvmStatic
        fun newInstance(items: ArrayList<MenuItem>) =
            RestaurantMenuFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ITEMS, items)
                }
            }
    }
}
