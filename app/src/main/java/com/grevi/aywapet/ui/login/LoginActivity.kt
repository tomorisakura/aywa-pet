package com.grevi.aywapet.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.ActivityLoginBinding
import com.grevi.aywapet.ui.keep.keeped.SuccessActivity
import com.grevi.aywapet.utils.SharedUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    @Inject lateinit var sharedUtils: SharedUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navController = findNavController(R.id.login_fragment_container)
        val appBar = AppBarConfiguration(setOf(
            R.id.loginFragment
        ))
        setupActionBarWithNavController(navController, appBar)
    }

    override fun onSupportNavigateUp(): Boolean {
        materialDialog().show()
        return true
    }

    private fun materialDialog() : MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(this)
            .setTitle("Apakah kamu pengen kembali ?")
            .setMessage("Proses pembuatan akun akan gagal loh")
            .setPositiveButton("Batal") { dialog, _, ->
                dialog.dismiss()
            }
            .setNeutralButton("Kembali") {dialog, _, ->
                sharedUtils.setLoginKeyDestroy()
                onBackPressed()
            }
    }
}

