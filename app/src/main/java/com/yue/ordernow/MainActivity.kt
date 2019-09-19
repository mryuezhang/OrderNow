package com.yue.ordernow

import android.os.Bundle
import android.util.SparseArray
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            startActivityForResult(OrderActivity.getStartActivityIntent(applicationContext), 0)
        }

        val menuPageViewAdapter = MenuPageViewAdapter(supportFragmentManager)
        menuViewPager.adapter = menuPageViewAdapter
        tablayout.setupWithViewPager(menuViewPager)
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
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MenuPageViewAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
        private var registeredFragments = SparseArray<Fragment>()

        override fun getItem(position: Int): Fragment = when(position) {
            0-> MainFragment()
            1-> DessertFragment()
            else-> DrinksFragment()
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence = when(position){
            0 -> "Main Dish"
            1 -> "Dessert"
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
}
