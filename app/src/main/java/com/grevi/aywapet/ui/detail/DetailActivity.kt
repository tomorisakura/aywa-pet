package com.grevi.aywapet.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.grevi.aywapet.databinding.ActivityDetailBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.detail.adapter.PicturesAdapter
import com.grevi.aywapet.ui.keep.keeped.KeepedActivity
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

class DetailActivity : BaseActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var picturesAdapter: PicturesAdapter
    private lateinit var snapHelper: LinearSnapHelper
    private lateinit var pagerSnapHelper: PagerSnapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initView()
    }

    private fun initView() {
        picturesAdapter = PicturesAdapter()
        snapHelper = LinearSnapHelper()
        pagerSnapHelper = PagerSnapHelper()

        val id = intent.getStringExtra("petId").toString()

        mainViewModel.getPet(id).observe(this, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> binding.pgLoading.visibility = View.VISIBLE
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.SUCCESS -> {

                    response.data?.result?.let { pet ->
                        with(binding) {
                            pgLoading.visibility = View.GONE
                            groupView.visibility = View.VISIBLE
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
                }
            }
        })

    }

    private fun observeChecker(pet : String) {
        mainViewModel.keepData.observe(this, {
            when(it.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, it.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, it.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, it.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    it.data?.result.let { state ->
                        if (state.isNullOrEmpty()) {
                            Intent(this@DetailActivity, KeepedActivity::class.java).apply {
                                putExtra("petId", pet)
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(this)
                            }
                        } else{
                            snackBar(binding.root, "Selesaikan terlebih dahulu keep kamu").show()
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        snackBar(binding.root, "resume")
    }

}