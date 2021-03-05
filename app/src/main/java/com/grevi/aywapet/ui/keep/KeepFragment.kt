package com.grevi.aywapet.ui.keep

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentKeepBinding
import com.grevi.aywapet.service.TimerService
import com.grevi.aywapet.ui.keep.adapter.KeepAdapter
import com.grevi.aywapet.ui.viewmodel.KeepViewModel
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant
import com.grevi.aywapet.utils.Constant.TIMER_BR
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeepFragment : Fragment() {

    private lateinit var binding : FragmentKeepBinding
    private val keepViewModel : KeepViewModel by viewModels()
    private val petViewModel : PetViewModel by viewModels()
    private val keepAdapter: KeepAdapter by lazy { KeepAdapter() }
    private val networkUtils: NetworkUtils by lazy { NetworkUtils(requireContext()) }
    private lateinit var intentFilter: IntentFilter

    private val TAG = KeepFragment::class.java.simpleName

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
        intentFilter = IntentFilter(TIMER_BR)
        setHasOptionsMenu(true)
        observeNetwork()
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                initView()
            }else{
                snackBar(root, getString(R.string.lost_network_text)).show()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.notif).isVisible = false
        menu.findItem(R.id.edit_account).isVisible = false
        menu.findItem(R.id.search).isVisible = false
    }

    private fun initView() = with(binding) {
        keepViewModel.keepData.observe(viewLifecycleOwner, { state ->
            when(state) {
                is State.Loading -> snackBar(root, state.msg).show()
                is State.Error -> snackBar(root, state.exception.toString()).show()
                is State.Success -> {
                    state.data.let {
                        if (it.result.isNullOrEmpty()) {
                            activity?.stopService(Intent(activity, TimerService::class.java))
                            snackBar(root, "kosong").show()
                        } else {
                            keepAdapter.addItem(it.result)
                            rvKeep.apply {
                                setHasFixedSize(true)
                                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                                adapter = keepAdapter
                            }
                            keepAdapter.onItemClick = { keep ->
                                BottomSheetBehavior.from(bottomSheet).also { bottomSheet ->
                                    petViewModel.getPet(keep.petId.id).observe(viewLifecycleOwner, { state ->
                                        when(state) {
                                            is State.Loading -> Log.i(TAG, state.msg)
                                            is State.Error -> Log.e(TAG, state.exception.toString())
                                            is State.Success -> {
                                                state.data.result.let { pet ->
                                                    Glide.with(root).load("${Constant.BASE_URL}/${pet.pictures[0].picUrl}").into(sheetImage)
                                                    sheetPetName.text = pet.petName
                                                    sheetTypePet.text = pet.types.jenis
                                                    sheetClinicName.text = pet.clinicId.name
                                                    sheetAddress.text = pet.clinicId.address
                                                }
                                            }
                                            else -> Unit
                                        }
                                    })
                                    bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }
        })


    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(timerReceiver, intentFilter)
    }

    override fun onStop() {
        try {
            activity?.unregisterReceiver(timerReceiver)
        } catch (e : IllegalArgumentException) {
            e.printStackTrace()
        }
        super.onStop()
    }

    override fun onDestroy() {
        activity?.stopService(Intent(activity, TimerService::class.java))
        super.onDestroy()
    }

    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val times = intent?.getStringExtra("times")
            binding.timeCount.text = times
            Log.i(TAG, times.toString())
        }

    }
}