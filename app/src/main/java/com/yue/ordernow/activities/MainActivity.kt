package com.yue.ordernow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yue.ordernow.R
import com.yue.ordernow.fragments.MenuFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private val menuItems = HashMap<Category, ArrayList<com.yue.ordernow.models.MenuItem>>()

    companion object {
        enum class Category {
            Appetizer,
            Breakfast,
            Maindish,
            Drink
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            menuItems[Category.Appetizer] = getMenuItemsFromFile(R.raw.appetizers)
            menuItems[Category.Breakfast] = getMenuItemsFromFile(R.raw.breakfast)
            menuItems[Category.Maindish] = getMenuItemsFromFile(R.raw.maindishes)
            menuItems[Category.Drink] = getMenuItemsFromFile(R.raw.drinks)
        } else {
            enumValues<Category>().forEach { category ->
                menuItems[category] = savedInstanceState.getParcelableArrayList(category.name)!!
            }
        }

        val menuPageViewAdapter = MenuPageViewAdapter(supportFragmentManager, menuItems)
        menuViewPager.adapter = menuPageViewAdapter
        tablayout.setupWithViewPager(menuViewPager)
    }

    override fun onSaveInstanceState(outState: Bundle) {

        enumValues<Category>().forEach { category ->
            outState.putParcelableArrayList(category.name, menuItems[category])
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_confirm -> {
                startOrderActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MenuPageViewAdapter(
        fm: FragmentManager,
        private val menuItems: HashMap<Category, ArrayList<com.yue.ordernow.models.MenuItem>>
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var registeredFragments = SparseArray<Fragment>()

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> MenuFragment(menuItems[Category.Appetizer]!!)
            1 -> MenuFragment(menuItems[Category.Breakfast]!!)
            2 -> MenuFragment(menuItems[Category.Maindish]!!)
            else -> MenuFragment(menuItems[Category.Drink]!!)
        }

        override fun getCount(): Int = 4

        override fun getPageTitle(position: Int): CharSequence = when (position) {
            0 -> "Appetizers"
            1 -> "Breakfast"
            2 -> "Maindishes"
            else -> "Drinks"
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

        fun getRegisteredFragment(position: Int): Fragment = registeredFragments.get(position)
    }

    private fun getMenuItemsFromFile(id: Int): ArrayList<com.yue.ordernow.models.MenuItem> {

        val items = ArrayList<com.yue.ordernow.models.MenuItem>()
        val reader = BufferedReader(InputStreamReader(resources.openRawResource(id)))
        var line = reader.readLine()
        var tokens: List<String>

        while (line != null) {
            tokens = line.split(",")

            try {
                items.add(com.yue.ordernow.models.MenuItem(tokens[0], tokens[1].toFloat(), 0))
            } catch (e: NumberFormatException) {
                items.add(com.yue.ordernow.models.MenuItem(tokens[0], 0.0F, 0))
            }

            line = reader.readLine()
        }

        return items
    }

    private fun startOrderActivity() {
        val orderActivityIntent = OrderActivity.getStartActivityIntent(this)

        enumValues<Category>().forEach { category ->
            orderActivityIntent.putParcelableArrayListExtra(category.name, menuItems[category])
        }

        startActivity(orderActivityIntent)
    }
}
