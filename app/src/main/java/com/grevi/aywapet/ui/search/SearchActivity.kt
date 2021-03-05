package com.grevi.aywapet.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivitySearchBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.category.adapter.CategoryAdapter
import com.grevi.aywapet.ui.detail.DetailActivity
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant.PET_ID
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val petViewModel : PetViewModel by viewModels()
    private val categoryAdapter : CategoryAdapter by lazy { CategoryAdapter() }
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(this) }
    private val job : Job by lazy { Job() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observeNetwork()
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(this@SearchActivity) {isConnect ->
            if(isConnect) initView() else snackBar(root, getString(R.string.lost_network_text)).show()
        }
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
                CoroutineScope(Dispatchers.Main + job).launch {
                    petViewModel.getSearchPet(s.toString()).observe(this@SearchActivity, { state ->
                        when(state) {
                            is State.Loading -> pgLoading.visibility = View.VISIBLE
                            is State.Error -> snackBar(root, state.exception.toString()).show()
                            is State.Success -> {
                                if (state.data.result.isNullOrEmpty()) {
                                    tvCount.text = "Menampilkan 0 Hasil"
                                    rvListResult.visibility = View.GONE
                                    pgLoading.visibility = View.VISIBLE
                                } else {
                                    pgLoading.visibility = View.GONE
                                    rvListResult.visibility = View.VISIBLE
                                    tvCount.text = "Menampilkan ${state.data.result.size} Hasil"
                                    rvListResult.setHasFixedSize(true)
                                    rvListResult.layoutManager = GridLayoutManager(this@SearchActivity, 2, GridLayoutManager.VERTICAL, false)
                                    rvListResult.adapter = categoryAdapter
                                    categoryAdapter.addItem(state.data.result)
                                    categoryAdapter.itemClickHelper = {
                                        Intent(this@SearchActivity, DetailActivity::class.java).apply {
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
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}