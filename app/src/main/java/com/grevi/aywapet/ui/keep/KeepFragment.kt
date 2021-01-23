package com.grevi.aywapet.ui.keep

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentKeepBinding
import com.grevi.aywapet.ui.keep.adapter.KeepAdapter
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KeepFragment : Fragment() {

    private lateinit var binding : FragmentKeepBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var keepAdapter: KeepAdapter

    @Inject lateinit var sharedUtils: SharedUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKeepBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        keepAdapter = KeepAdapter()
        mainViewModel.keepData.observe(viewLifecycleOwner, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    response.data?.let {
                        if (it.result.isNullOrEmpty()) {
                            snackBar(binding.root, "kosong").show()
                        } else {
                            val timer = sharedUtils.getCountDownShared()!!
                            keepAdapter.addItem(it.result, timer)
                            binding.rvKeep.setHasFixedSize(true)
                            binding.rvKeep.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                            binding.rvKeep.adapter = keepAdapter
                        }
                    }
                }
            }
        })
    }
}