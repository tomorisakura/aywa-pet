package com.grevi.aywapet.ui.keep.keeped

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityKeepedBinding
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Constant.ARG_TIMER
import com.grevi.aywapet.utils.Constant.BASE_URL
import com.grevi.aywapet.utils.Constant.TIMER_WORKER_TAG
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.snackBar
import com.grevi.aywapet.worker.TimerWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class KeepedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityKeepedBinding
    private val mainViewModel : MainViewModel by viewModels()

    private var cbState : Boolean = false

    @Inject lateinit var sharedUtils: SharedUtils

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

                            btnKeep.setOnClickListener {
                                if (cbState) {
                                    materialDialog("Terima kasih telah menerima terms & condition dari Aywa Pet, pastikan kamu memenuhi syarat ya 😍", pet.id).show()
                                } else {
                                    snackBar(binding.root, "Anda Belum Menyetujui Terms & Condition").show()
                                }
                            }
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
            it?.let {
                //binding.timer.text = it
                setWorker(it)
            }
        })

        binding.cbAcceptTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                cbState = isChecked
            } else {
                cbState = isChecked
                snackBar(binding.root, "Anda Belum Menyetujui Terms & Condition").show()
            }
        }
    }

    private fun setWorker(value : String) {
        val data = Data.Builder()
        data.putString("Time", value)
        val timeWorker = OneTimeWorkRequest.from(TimerWorker::class.java)
        val wm = WorkManager.getInstance(this@KeepedActivity)
        wm.getWorkInfoByIdLiveData(timeWorker.id)
                .observe(this) {
                    if (it != null) {
                        val output = it.progress.getInt(ARG_TIMER, 0)
                        Log.d("WORK_INFO", output.toString())
                        //binding.timer.text = output.toString()
                    } else {
                        Log.d("WORK_INFO", "output null")
                    }
                }

        wm.enqueue(timeWorker)

    }

    private fun materialDialog( msg : String, id : String) : MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(this)
            .setTitle("Keeped")
            .setMessage(msg)
            .setPositiveButton("Ok, OTW ") { dialog, _, ->
                mainViewModel.keepPostData(id).observe(this, {response ->
                    when(response.status) {
                        Resource.STATUS.LOADING -> snackBar(binding.root, response.msg!!).show()
                        Resource.STATUS.ERROR -> snackBar(binding.root, response.msg!!).show()
                        Resource.STATUS.EXCEPTION -> snackBar(binding.root, response.msg!!).show()
                        Resource.STATUS.SUCCESS -> {
                            mainViewModel.setTimer()
                            Intent(this, SuccessActivity::class.java).apply {
                                startActivity(this)
                                finish()
                            }
                        }
                    }
                })
            }
            .setNeutralButton("Batal") {dialog, _, ->
                dialog.dismiss()
            }
    }
}