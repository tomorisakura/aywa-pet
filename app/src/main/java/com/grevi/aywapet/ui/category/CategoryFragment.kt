package com.grevi.aywapet.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.databinding.FragmentCategoryBinding
import com.grevi.aywapet.ui.category.adapter.CategoryAdapter
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val args : CategoryFragmentArgs by navArgs()
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        categoryAdapter = CategoryAdapter()
        mainViewModel.getPetByType(args.categoryId).observe(viewLifecycleOwner, { resp ->
            when(resp.status) {
                Resource.STATUS.LOADING -> resp.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.ERROR -> resp.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.EXCEPTION -> resp.msg?.let { snackBar(binding.root, it).show() }
                Resource.STATUS.SUCCESS -> {
                    resp.data?.result?.let {
                        if (it.isNullOrEmpty()) {
                            snackBar(binding.root, "Category Ini Belum Terisi 😅").show()
                        } else {
                            with(binding) {
                                categoryText.text = args.categoryName
                                rvListPetCategory.setHasFixedSize(true)
                                rvListPetCategory.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                                rvListPetCategory.adapter = categoryAdapter
                                categoryAdapter.addItem(it)
                                categoryAdapter.itemClickHelper = {
                                    Intent(requireActivity(), DetailActivity::class.java).apply {
                                        putExtra("petId", it.id)
                                        startActivity(this)
                                    }
                                }
                            }
                        }
                    }
                    //end of 
                }
            }
        })
    }
}