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
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentHomeBinding
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.home.adapter.PetsAdapter
import com.grevi.aywapet.ui.home.adapter.TypesAdapter
import com.grevi.aywapet.ui.viewmodel.CategoryViewModel
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant.PET_ID
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()

    private lateinit var typesAdapter: TypesAdapter
    private lateinit var petsAdapter: PetsAdapter

    private lateinit var navController: NavController

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

    private fun initTypes() = with(binding) {
        typesAdapter = TypesAdapter()
        petsAdapter = PetsAdapter()

        categoryViewModel.animalData.observe(viewLifecycleOwner, { response ->
            when (response.status) {
                Resource.STATUS.LOADING -> {
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                Resource.STATUS.ERROR -> {
                    response.msg?.let { snackBar(root, it).show() }
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                Resource.STATUS.EXCEPTION -> {
                    response.msg?.let { snackBar(root, it).show() }
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                Resource.STATUS.SUCCESS -> {
                    shimmer.visibility = View.GONE
                    itemViewGroup.visibility = View.VISIBLE
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
        })

        petViewModel.petData.observe(viewLifecycleOwner, { response ->
            when (response.status) {
                Resource.STATUS.LOADING -> {
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                Resource.STATUS.ERROR -> {
                    response.msg?.let { snackBar(root, it).show() }
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                Resource.STATUS.EXCEPTION -> {
                    response.msg?.let { snackBar(root, it).show() }
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                Resource.STATUS.SUCCESS -> {
                    shimmer.visibility = View.GONE
                    itemViewGroup.visibility = View.VISIBLE
                    rvItemPet.setHasFixedSize(true)
                    rvItemPet.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvItemPet.adapter = petsAdapter
                    response.data?.result?.let { petsAdapter.addItem(it) }

                    petsAdapter.itemClickHelper = {
                        Intent(activity, DetailActivity::class.java).apply {
                            putExtra(PET_ID, it.id)
                            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(this)
                            activity?.overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.shimmer.startShimmer()
    }

    override fun onPause() {
        binding.shimmer.stopShimmer()
        super.onPause()
    }

}