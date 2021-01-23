package com.grevi.aywapet.ui.keep.keeped

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.grevi.aywapet.databinding.ActivitySuccessBinding
import com.grevi.aywapet.ui.base.BaseActivity
import com.grevi.aywapet.ui.home.HomeActivity

class SuccessActivity : BaseActivity() {

    private lateinit var binding : ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener {
            Intent(this, HomeActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }


}