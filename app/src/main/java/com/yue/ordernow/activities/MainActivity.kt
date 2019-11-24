package com.yue.ordernow.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.yue.ordernow.R
import com.yue.ordernow.databinding.ActivityMainBinding
import com.yue.ordernow.fragments.RestaurantMenuFragment.Companion.taxRate
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.utilities.currencySign
import com.yue.ordernow.viewModels.MainViewModel


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var appBarConfiguration: AppBarConfiguration

    val viewModel: MainViewModel by viewModels {
        InjectorUtils.provideMainViewModelFactory(applicationContext)
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_restaurant_menu,
                R.id.nav_recent_orders,
                R.id.nav_dashboard,
                R.id.nav_settings
            ), binding.drawerLayout
        )

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        val stringTaxRate: String? = PreferenceManager.getDefaultSharedPreferences(this).getString(
            resources.getString(R.string.key_tax_rate),
            resources.getString(R.string.default_tax_rate)
        )
        if (!stringTaxRate.isNullOrBlank()) {
            taxRate = stringTaxRate.toFloat() / 100
        }

    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String) {
        if (p1 == resources.getString(R.string.key_tax_rate)) {
            p0?.getString(p1, "0")?.let {
                try {
                    taxRate = it.toFloat() / 100
                } catch (e: NumberFormatException) {

                }
            }
        }

        if (p1 == resources.getString(R.string.key_currency)) {
            currencySign = when (p0?.getString(p1, "")) {
                "1" -> "\u20ac"
                "2" -> "\u00a3"
                else -> "$"
            }
        }
    }

}
