package com.grevi.aywapet.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.grevi.aywapet.databinding.FragmentSplashBinding
import com.grevi.aywapet.ui.home.HomeActivity
import com.grevi.aywapet.ui.login.LoginActivity
import com.grevi.aywapet.utils.SharedUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding : FragmentSplashBinding
    private lateinit var navController : NavController

    @Inject lateinit var sharedUtils: SharedUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                sharedUtils.getIntroShared() -> {
                    Intent(requireActivity(), LoginActivity::class.java).apply {
                        startActivity(this)
                        activity?.finish()
                    }
                }

                sharedUtils.getLoginShared() -> {
                    Intent(requireActivity(), HomeActivity::class.java).apply {
                        startActivity(this)
                        activity?.finish()
                    }
                }

                else -> {
                    val action = SplashFragmentDirections.actionSplashFragmentToIntroViewPagerFragment()
                    navController.navigate(action)
                }
            }
        }, 3000L)
    }
}