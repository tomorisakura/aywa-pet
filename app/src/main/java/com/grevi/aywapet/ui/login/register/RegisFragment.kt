package com.grevi.aywapet.ui.login.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.grevi.aywapet.databinding.FragmentRegisBinding
import com.grevi.aywapet.ui.home.HomeActivity
import com.grevi.aywapet.ui.viewmodel.RegisViewModel
import com.grevi.aywapet.utils.RegisHelper
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisFragment : Fragment(), RegisHelper {

    private lateinit var binding : FragmentRegisBinding
    private val args : RegisFragmentArgs by navArgs()

    private val regisViewModel : RegisViewModel by viewModels()

    @Inject lateinit var sharedUtils: SharedUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisBinding.inflate(inflater)
        binding.data = regisViewModel
        regisViewModel.regisHelper = this
        regisViewModel.name.value = args.name
        regisViewModel.email.value = args.email
        regisViewModel.uid.value = args.uid
        return binding.root
    }

    override fun success(id : String) {
        //run intent to home
        sharedUtils.setLoginKey()
        sharedUtils.setUserKey(id)

        regisViewModel.getEmailVerify(id).observe(viewLifecycleOwner, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    response.data?.result?.let {
                        val token = response.data.token
                    }
                    Intent(activity, HomeActivity::class.java).apply {
                        startActivity(this)
                        activity?.finish()
                    }
                }
            }
        })
    }

    override fun message(msg: String) {
        snackBar(binding.root, msg).show()
    }
}