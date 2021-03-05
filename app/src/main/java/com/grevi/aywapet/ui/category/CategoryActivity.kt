package com.grevi.aywapet.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityCategoryBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.category.adapter.CategoryAdapter
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.search.SearchActivity
import com.grevi.aywapet.ui.viewmodel.CategoryViewModel
import com.grevi.aywapet.utils.Constant.CATEGORY_ID
import com.grevi.aywapet.utils.Constant.CATEGORY_NAME
import com.grevi.aywapet.utils.Constant.PET_ID
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar

class CategoryActivity : BaseActivity() {

    private lateinit var binding : ActivityCategoryBinding
    private val categoryViewModel : CategoryViewModel by viewModels()
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.category_text)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observeNetwork()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.notif)?.isVisible = false
        menu?.findItem(R.id.edit_account)?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> {
                Intent(this, SearchActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() = with(binding) {
        val id = intent.getStringExtra(CATEGORY_ID)!!
        val name = intent.getStringExtra(CATEGORY_NAME)!!
        categoryViewModel.getPetByType(id).observe(this@CategoryActivity, { state ->
            when(state) {
                is State.Loading -> snackBar(root, state.msg).show()
                is State.Error -> snackBar(root, state.exception.toString()).show()
                is State.Success -> {
                    if (state.data.result.isNullOrEmpty()) {
                        snackBar(root, "Category Ini Belum Terisi 😅").show()
                    } else {
                        categoryText.text = name
                        tvCount.text = "Menampilkan ${state.data.result.size} Hasil"
                        rvListPetCategory.setHasFixedSize(true)
                        rvListPetCategory.layoutManager = GridLayoutManager(this@CategoryActivity, 2, GridLayoutManager.VERTICAL, false)
                        rvListPetCategory.adapter = categoryAdapter
                        categoryAdapter.addItem(state.data.result)
                        categoryAdapter.itemClickHelper = {
                            Intent(this@CategoryActivity, DetailActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                putExtra(PET_ID, it.id)
                                startActivity(this)
                            }
                        }
                    }
                }
                else -> Unit
            }
        })
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(this@CategoryActivity) { isConnect ->
            if (isConnect) {
                initView()
            }else {
                snackBar(root, getString(R.string.lost_network_text)).show()
            }
        }
    }
}