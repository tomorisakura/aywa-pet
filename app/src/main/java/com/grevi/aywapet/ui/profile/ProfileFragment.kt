package com.grevi.aywapet.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.databinding.FragmentProfileBinding
import com.grevi.aywapet.ui.profile.adapter.KeepSuccessAdapter
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var keepSuccessAdapter: KeepSuccessAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mainViewModel.userTable.observe(viewLifecycleOwner, {
            with(binding) {
                tvUsername.text = it[0].username
                tvEmail.text = it[0].email
            }
        })

        mainViewModel.keepSuccessData.observe(viewLifecycleOwner, { resp ->
            when(resp.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, resp.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, resp.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, resp.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    resp.data?.result?.let {
                        with(binding) {
                            keepSuccessAdapter = KeepSuccessAdapter(it)
                            rvListKeepStatus.setHasFixedSize(true)
                            rvListKeepStatus.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                            rvListKeepStatus.adapter = keepSuccessAdapter
                        }
                    }
                }
            }
        })

    }
}