package com.grevi.aywapet.ui.category

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityCategoryBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.category.adapter.CategoryAdapter
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.viewmodel.CategoryViewModel
import com.grevi.aywapet.utils.Constant.CATEGORY_ID
import com.grevi.aywapet.utils.Constant.CATEGORY_NAME
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar

class CategoryActivity : BaseActivity() {

    private lateinit var binding : ActivityCategoryBinding
    private val categoryViewModel : CategoryViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.category_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent.also {
            val id = it.getStringExtra(CATEGORY_ID)!!
            val name = it.getStringExtra(CATEGORY_NAME)!!
            initView(id, name)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView(id : String, name : String) = with(binding) {
        categoryAdapter = CategoryAdapter()
        categoryViewModel.getPetByType(id).observe(this@CategoryActivity, { resp ->
            when(resp.status) {
                Resource.STATUS.LOADING -> resp.msg?.let { snackBar(root, it).show() }
                Resource.STATUS.ERROR -> resp.msg?.let { snackBar(root, it).show() }
                Resource.STATUS.EXCEPTION -> resp.msg?.let { snackBar(root, it).show() }
                Resource.STATUS.SUCCESS -> {
                    resp.data?.result?.let {
                        if (it.isNullOrEmpty()) {
                            snackBar(root, "Category Ini Belum Terisi 😅").show()
                        } else {
                            categoryText.text = name
                            tvCount.text = "Menampilkan ${it.size} Hasil"
                            rvListPetCategory.setHasFixedSize(true)
                            rvListPetCategory.layoutManager = GridLayoutManager(this@CategoryActivity, 2, GridLayoutManager.VERTICAL, false)
                            rvListPetCategory.adapter = categoryAdapter
                            categoryAdapter.addItem(it)
                            categoryAdapter.itemClickHelper = {
                                Intent(this@CategoryActivity, DetailActivity::class.java).apply {
                                    putExtra("petId", it.id)
                                    startActivity(this)
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