package com.grevi.aywapet.ui.intro

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityIntroBinding
import com.grevi.aywapet.ui.base.BaseActivity

class IntroActivity : BaseActivity() {

    private lateinit var binding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.intro_fragment_container) as NavHostFragment
        navHostFragment.navController
    }
}