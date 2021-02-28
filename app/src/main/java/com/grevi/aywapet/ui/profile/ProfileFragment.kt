package com.grevi.aywapet.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentProfileBinding
import com.grevi.aywapet.ui.profile.adapter.KeepSuccessAdapter
import com.grevi.aywapet.ui.viewmodel.KeepViewModel
import com.grevi.aywapet.ui.viewmodel.ProfileViewModel
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val profileViewModel : ProfileViewModel by viewModels()
    private val keepViewModel : KeepViewModel by viewModels()
    private lateinit var keepSuccessAdapter: KeepSuccessAdapter

    private val TAG = ProfileFragment::class.java.simpleName

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
        setHasOptionsMenu(true)
        initView()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.notif).isVisible = false
        menu.findItem(R.id.edit_account).isVisible = true
    }

    private fun initView() = with(binding) {
        lifecycleScope.launchWhenCreated {
            profileViewModel.getUserLocalFlow.collect { state ->
                when(state) {
                    is State.Loading -> Log.d(TAG, state.msg)
                    is State.Error -> Log.e(TAG, state.exception.toString())
                    is State.Success -> {
                        state.data.forEach { users ->
                            tvUsername.text = users.username
                            tvEmail.text = users.email
                            tvAddress.text = users.address
                        }
                    }
                    else -> Unit
                }
            }
        }

        keepViewModel.keepSuccessData.observe(viewLifecycleOwner, { resp ->
            when(resp.status) {
                Resource.STATUS.LOADING -> snackBar(root, resp.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(root, resp.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(root, resp.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    resp.data?.result?.let {
                        keepSuccessAdapter = KeepSuccessAdapter(it)
                        rvListKeepStatus.setHasFixedSize(true)
                        rvListKeepStatus.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                        rvListKeepStatus.adapter = keepSuccessAdapter
                    }
                }
            }
        })

    }
}