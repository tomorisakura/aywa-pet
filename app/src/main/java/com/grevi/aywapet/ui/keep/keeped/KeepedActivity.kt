package com.grevi.aywapet.ui.keep.keeped

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityKeepedBinding
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Constant.BASE_URL
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class KeepedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityKeepedBinding
    private val mainViewModel : MainViewModel by viewModels()

    private var cbState : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeepedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Keeped"
        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView() {
        val id = intent.getStringExtra("petId").toString()
        mainViewModel.getPet(id).observe(this, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    response.data?.result?.let {pet ->
                        with(binding) {
                            supportActionBar?.subtitle = pet.petName
                            Glide.with(this@KeepedActivity).load("$BASE_URL/${pet.pictures[0].picUrl}").placeholder(R.drawable.ic_image_placeholder).into(picPet)
                            petName.text = pet.petName
                            petType.text = pet.types.jenis
                            petClinic.text = pet.clinicId.name
                            addressClinic.text = pet.clinicId.address
                        }
                    }
                }

            }
        })
        listOf(getString(R.string.terms1), getString(R.string.terms2), getString(R.string.terms3), getString(R.string.terms4)).apply {
            val termAdapter = ArrayAdapter(this@KeepedActivity, R.layout.list_terms, R.id.terms, this)
            binding.listTerms.adapter = termAdapter
        }

        mainViewModel.timeCount.observe(this, {
            it?.let { binding.timer.text = it }
        })

        binding.cbAcceptTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                cbState = isChecked
            } else {
                cbState = isChecked
                snackBar(binding.root, "Anda Belum Menyetujui Terms & Condition").show()
            }
        }

        binding.btnKeep.setOnClickListener {
            if (cbState) {
                materialDialog("Terima kasih telah menerima terms & condition dari Aywa Pet, pastikan kamu memenuhi syarat ya 😍").show()
            } else {
                snackBar(binding.root, "Anda Belum Menyetujui Terms & Condition").show()
            }
        }
    }

    private fun materialDialog( msg : String) : MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(this)
            .setTitle("Keeped")
            .setMessage(msg)
            .setPositiveButton("Ok, OTW ") { dialog, _, ->
                mainViewModel.setTimer()
                Intent(this, SuccessActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
            .setNeutralButton("Batal") {dialog, _, ->
                dialog.dismiss()
            }
    }
}