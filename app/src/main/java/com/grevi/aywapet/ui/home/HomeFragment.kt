package com.grevi.aywapet.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentHomeBinding
import com.grevi.aywapet.ui.category.CategoryActivity
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.home.adapter.PetsAdapter
import com.grevi.aywapet.ui.home.adapter.TypesAdapter
import com.grevi.aywapet.ui.notif.NotificationActivity
import com.grevi.aywapet.ui.viewmodel.CategoryViewModel
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant.CATEGORY_ID
import com.grevi.aywapet.utils.Constant.CATEGORY_NAME
import com.grevi.aywapet.utils.Constant.PET_ID
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels()
    private val typesAdapter: TypesAdapter by lazy { TypesAdapter() }
    private val petsAdapter: PetsAdapter by lazy { PetsAdapter() }
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(requireContext()) }
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
        setHasOptionsMenu(true)
        observeNetwork()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.edit_account).isVisible = false
        menu.findItem(R.id.search).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.notif -> {
                Intent(activity, NotificationActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() = with(binding) {
        categoryViewModel.animalData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                is State.Error -> {
                    snackBar(root, state.exception.toString()).show()
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                is State.Success -> {
                    shimmer.visibility = View.GONE
                    itemViewGroup.visibility = View.VISIBLE
                    rvListTypes.apply {
                        setHasFixedSize(true)
                        animate().alpha(1f).duration = 1000L
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        adapter = typesAdapter
                        typesAdapter.addItem(state.data.result)
                        typesAdapter.typeItemClicked = {
                            Intent(activity, CategoryActivity::class.java).apply {
                                putExtra(CATEGORY_ID, it.id)
                                putExtra(CATEGORY_NAME, it.jenis)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(this)
                            }
                        }
                    }
                }
                else -> Unit
            }
        })

        petViewModel.petData.observe(viewLifecycleOwner, { state ->
            when (state) {
                is State.Loading -> {
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                is State.Error -> {
                    snackBar(root, state.exception.toString()).show()
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                    itemViewGroup.visibility = View.GONE
                }
                is State.Success -> {
                    shimmer.visibility = View.GONE
                    itemViewGroup.visibility = View.VISIBLE
                    rvItemPet.apply {
                        animate().alpha(1f).duration = 1000L
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        adapter = petsAdapter
                    }
                    petsAdapter.addItem(state.data.result)
                    petsAdapter.itemClickHelper = {
                        Intent(activity, DetailActivity::class.java).apply {
                            putExtra(PET_ID, it.id)
                            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(this)
                            activity?.overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                        }
                    }
                }
                else -> Unit
            }
        })
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                shimmer.stopShimmer()
                initView()
            }else {
                shimmer.startShimmer()
                snackBar(root, getString(R.string.lost_network_text)).show()
            }
        }
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