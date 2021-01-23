package com.grevi.aywapet.ui.home

import android.os.Bundle
import android.util.Log
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
            //handle navigation the users if they dont have key or not sign in
            when(destination.id) {
                R.id.navigation_keep -> {
                    Log.v(TAG, "keep")
                }
                R.id.navigation_account -> {
                    Log.v(TAG, "akun")
                }
            }
        }
    }
}