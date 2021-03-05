package com.grevi.aywapet.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.appbar.AppBarLayout
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityDetailBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.detail.adapter.PicturesAdapter
import com.grevi.aywapet.ui.keep.keeped.KeepedActivity
import com.grevi.aywapet.ui.viewmodel.KeepViewModel
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant.PET_ID
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar

class DetailActivity : BaseActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val petViewModel : PetViewModel by viewModels()
    private val keepViewModel : KeepViewModel by viewModels()

    private val picturesAdapter: PicturesAdapter by lazy { PicturesAdapter() }
    private val snapHelper: LinearSnapHelper by lazy { LinearSnapHelper() }
    private val pagerSnapHelper: PagerSnapHelper by lazy { PagerSnapHelper() }
    private val networkUtils : NetworkUtils by lazy { NetworkUtils(this) }

    private val TAG = DetailActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        observeNetwork()
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(this@DetailActivity) { isConnect ->
            if (isConnect) {
                initView()
            }else{
                snackBar(root, getString(R.string.lost_network_text)).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView() = with(binding) {
        val id = intent.getStringExtra(PET_ID).toString()
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                appBarLayout.totalScrollRange + verticalOffset == 0 -> supportActionBar?.setDisplayHomeAsUpEnabled(true)
                else -> supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        })

        petViewModel.getPet(id).observe(this@DetailActivity, { state ->
            when(state) {
                is State.Loading -> {
                    with(binding) {
                        appbar.visibility = View.GONE
                        btnAdopt.visibility = View.GONE
                        pgLoading.visibility = View.VISIBLE
                    }
                }
                is State.Error -> snackBar(root, state.exception.toString()).show()
                is State.Success -> {
                    state.data.result.let { pet ->
                        appbar.visibility = View.VISIBLE
                        pgLoading.visibility = View.GONE
                        groupView.visibility = View.VISIBLE
                        btnAdopt.visibility = View.VISIBLE
                        supportActionBar?.title = pet.petName
                        supportActionBar?.subtitle = "Owner Lama ${pet.oldOwner}"
                        picturesAdapter.addItem(pet.pictures)
                        tvClinicName.text = pet.clinicId.name
                        tvAddressClinic.text = pet.clinicId.address
                        vaccinePet.text = pet.vaccine
                        typePet.text = pet.types.jenis
                        genderPet.text = pet.gender
                        weightPet.text = pet.weight
                        rasPet.text = pet.ras
                        agePet.text = pet.age
                        tvInfo.text = pet.info

                        rvPhotos.setHasFixedSize(true)
                        rvPhotos.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
                        rvPhotos.adapter = picturesAdapter
                        snapHelper.attachToRecyclerView(rvPhotos)
                        indicator.attachToRecyclerView(rvPhotos, pagerSnapHelper)

                        btnAdopt.setOnClickListener {
                            observeChecker(pet.id)
                        }
                    }
                }
                else -> Unit
            }
        })

    }

    private fun observeChecker(pet : String) = with(binding) {
        keepViewModel.keepData.observe(this@DetailActivity, { state ->
            when(state) {
                is State.Loading -> snackBar(root, state.msg).show()
                is State.Error -> snackBar(root, state.exception.toString()).show()
                is State.Success -> {
                    state.data.result.let { keep ->
                        if (keep.isNullOrEmpty()) {
                            Intent(this@DetailActivity, KeepedActivity::class.java).apply {
                                putExtra(PET_ID, pet)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(this)
                            }
                        } else{
                            snackBar(root, "Selesaikan terlebih dahulu keep kamu").show()
                        }
                    }
                }
                else -> Unit
            }
        })
    }

}