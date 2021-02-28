package com.grevi.aywapet.ui.notif

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.notification_text)
    }
}