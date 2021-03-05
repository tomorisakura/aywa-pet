package com.grevi.aywapet.ui.keep.keeped

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityKeepedBinding
import com.grevi.aywapet.service.TimerService
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.viewmodel.KeepViewModel
import com.grevi.aywapet.ui.viewmodel.PetViewModel
import com.grevi.aywapet.utils.Constant.BASE_URL
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import java.util.*
import javax.inject.Inject

class KeepedActivity : BaseActivity() {

    private lateinit var binding : ActivityKeepedBinding
    private val keepViewModel : KeepViewModel by viewModels()
    private val petViewModel : PetViewModel by viewModels()
    private val networkUtils: NetworkUtils by lazy { NetworkUtils(this) }
    private var cbState : Boolean = false

    private val TAG = KeepedActivity::class.java.simpleName

    @Inject lateinit var sharedUtils: SharedUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeepedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adopsi"
        observeNetwork()
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(this@KeepedActivity) { isConnect ->
            if (isConnect) initView() else snackBar(root, getString(R.string.lost_network_text)).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initView() = with(binding) {
        val id = intent.getStringExtra("petId").toString()
        petViewModel.getPet(id).observe(this@KeepedActivity, { state ->
            when(state) {
                is State.Loading -> snackBar(root, state.msg).show()
                is State.Error -> snackBar(root, state.exception.toString()).show()
                is State.Success -> {
                    state.data.result.let {pet ->
                        supportActionBar?.subtitle = pet.petName
                        Glide.with(this@KeepedActivity).load("$BASE_URL/${pet.pictures[0].picUrl}").placeholder(R.drawable.ic_image_placeholder).into(picPet)
                        petName.text = pet.petName
                        petType.text = pet.types.jenis
                        petClinic.text = pet.clinicId.name
                        addressClinic.text = pet.clinicId.address

                        btnKeep.setOnClickListener {
                            if (cbState) {
                                materialDialog("Terima kasih telah menerima terms & condition dari Aywa Pet, pastikan kamu memenuhi syarat ya 😍", pet.id).show()
                            } else {
                                snackBar(root, "Anda Belum Menyetujui Syarat & Ketentuan").show()
                            }
                        }
                    }
                }

                else -> Unit
            }
        })
        listOf(getString(R.string.terms1), getString(R.string.terms2), getString(R.string.terms3), getString(R.string.terms4)).apply {
            val termAdapter = ArrayAdapter(this@KeepedActivity, R.layout.list_terms, R.id.terms, this)
            listTerms.adapter = termAdapter
        }

        cbAcceptTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                cbState = isChecked
            } else {
                cbState = isChecked
                snackBar(root, "Anda Belum Menyetujui Terms & Condition").show()
            }
        }
    }

    private fun materialDialog( msg : String, id : String) : MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(this)
            .setTitle("Keeped")
            .setMessage(msg)
            .setPositiveButton("Ok, OTW ") { dialog, _, ->
                keepViewModel.keepPostData(id).observe(this, {state ->
                    when(state) {
                        is State.Loading -> snackBar(binding.root, state.msg).show()
                        is State.Error -> snackBar(binding.root, state.exception.toString()).show()
                        is State.Success -> {
                            startService(Intent(this, TimerService::class.java))
                            Intent(this, SuccessActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(this)
                            }
                        }
                        else -> Unit
                    }
                })
            }
            .setNegativeButton("Batal") {dialog, _, ->
                dialog.dismiss()
            }
    }
}