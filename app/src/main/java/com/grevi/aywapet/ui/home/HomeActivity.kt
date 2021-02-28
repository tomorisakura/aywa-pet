package com.grevi.aywapet.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityHomeBinding
import com.grevi.aywapet.ui.base.BaseActivity

class HomeActivity : BaseActivity(){

    private lateinit var binding : ActivityHomeBinding
    private val TAG = HomeActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.app_fragment_container)
        val appBar = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_keep, R.id.navigation_account
        ))
        setupActionBarWithNavController(navController, appBar)
        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            //handle navigation properties
            when(destination.id) {
                R.id.navigation_keep -> binding.searchCard.visibility = View.GONE
                R.id.navigation_account -> binding.searchCard.visibility = View.GONE
                R.id.navigation_home -> binding.searchCard.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }
}