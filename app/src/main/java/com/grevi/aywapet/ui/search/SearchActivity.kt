package com.grevi.aywapet.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.databinding.ActivitySearchBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.category.adapter.CategoryAdapter
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant.PET_ID
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar

class SearchActivity : BaseActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val petViewModel : PetViewModel by viewModels()
    private val categoryAdapter : CategoryAdapter by lazy { CategoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView() = with(binding) {
        pgLoading.visibility = View.GONE
        edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                petViewModel.getSearchPet(s.toString()).observe(this@SearchActivity, { resp ->
                    when(resp.status) {
                        Resource.STATUS.LOADING -> pgLoading.visibility = View.VISIBLE
                        Resource.STATUS.ERROR -> resp.msg?.let { snackBar(root, it).show() }
                        Resource.STATUS.EXCEPTION -> resp.msg?.let { snackBar(root, it).show() }
                        Resource.STATUS.SUCCESS -> {
                            resp.data?.result?.let {
                                if (it.isNullOrEmpty()) {
                                    tvCount.text = "Menampilkan 0 Hasil"
                                    rvListResult.visibility = View.GONE
                                    pgLoading.visibility = View.VISIBLE
                                } else {
                                    pgLoading.visibility = View.GONE
                                    rvListResult.visibility = View.VISIBLE
                                    tvCount.text = "Menampilkan ${it.size} Hasil"
                                    rvListResult.setHasFixedSize(true)
                                    rvListResult.layoutManager = GridLayoutManager(this@SearchActivity, 2, GridLayoutManager.VERTICAL, false)
                                    rvListResult.adapter = categoryAdapter
                                    categoryAdapter.addItem(it)
                                    categoryAdapter.itemClickHelper = {
                                        Intent(this@SearchActivity, DetailActivity::class.java).apply {
                                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            putExtra(PET_ID, it.id)
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

        })
    }
}