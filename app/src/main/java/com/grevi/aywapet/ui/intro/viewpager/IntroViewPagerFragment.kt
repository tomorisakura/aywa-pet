package com.grevi.aywapet.ui.intro.viewpager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentIntroViewPagerBinding
import com.grevi.aywapet.ui.home.HomeActivity
import com.grevi.aywapet.ui.intro.IntroOneFragment
import com.grevi.aywapet.ui.intro.IntroThreeFragment
import com.grevi.aywapet.ui.intro.IntroTwoFragment
import com.grevi.aywapet.ui.login.LoginActivity
import com.grevi.aywapet.utils.Constant
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.snackBar
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroViewPagerFragment : Fragment() {

    private lateinit var binding : FragmentIntroViewPagerBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @Inject lateinit var sharedUtils: SharedUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroViewPagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val listFragment : List<Fragment> = listOf(
            IntroOneFragment(),
            IntroTwoFragment(),
            IntroThreeFragment()
        )
        viewPagerAdapter = ViewPagerAdapter(listFragment, childFragmentManager, lifecycle)
        with(binding) {
            introVp.adapter = viewPagerAdapter
            indicator.setViewPager(introVp)
            introVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(position) {
                        2 -> {
                            btnIntro.text = getString(R.string.finish_text)
                            btnIntro.setOnClickListener {
                                storageHandler()
                            }
                        }
                        else -> {
                            btnIntro.text = getString(R.string.next_text)
                            btnIntro.setOnClickListener {
                                introVp.currentItem += 1
                            }
                        }
                    }
                }
            })
        }
    }

    private fun storageHandler() {
        PermissionX.init(activity)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(deniedList, "Sebelum memakai aplikasi Aywa Pet anda harus menyetujui permission berikut", "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    sharedUtils.setIntroShared()
                    Intent(activity, LoginActivity::class.java).apply {
                        startActivity(this)
                        activity?.finish()
                    }
                } else {
                    snackBar(binding.root, "Permission Deny").show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        activity?.finish()
                    }, 1000L)
                }
            }
    }

}