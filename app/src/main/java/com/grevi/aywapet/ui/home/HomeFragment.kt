package com.grevi.aywapet.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.aywapet.databinding.FragmentHomeBinding
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.home.adapter.PetsAdapter
import com.grevi.aywapet.ui.home.adapter.TypesAdapter
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var typesAdapter: TypesAdapter
    private lateinit var petsAdapter: PetsAdapter

    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initTypes()
    }

    private fun initTypes() {
        typesAdapter = TypesAdapter()
        petsAdapter = PetsAdapter()

        mainViewModel.animalData.observe(viewLifecycleOwner, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> response.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.ERROR -> response.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.EXCEPTION -> response.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.SUCCESS -> {
                    with(binding) {
                        rvListTypes.setHasFixedSize(true)
                        rvListTypes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        rvListTypes.adapter = typesAdapter
                        response.data?.result?.let { typesAdapter.addItem(it) }

                        typesAdapter.typeItemClicked = {
                            HomeFragmentDirections.actionNavigationHomeToCategoryFragment(it.id, it.jenis).apply {
                                navController.navigate(this)
                            }
                        }
                    }
                }
            }
        })

        mainViewModel.petData.observe(viewLifecycleOwner, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> response.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.ERROR -> response.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.EXCEPTION -> response.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.SUCCESS -> {
                    with(binding) {
                        rvItemPet.setHasFixedSize(true)
                        rvItemPet.layoutManager = GridLayoutManager(requireContext(), 2)
                        rvItemPet.adapter = petsAdapter
                        response.data?.result?.let { petsAdapter.addItem(it) }

                        petsAdapter.itemClickHelper = {
                            Intent(activity, DetailActivity::class.java).apply {
                                putExtra("petId", it.id)
                                startActivity(this)
                            }
                        }
                    }
                }
            }
        })
    }

}