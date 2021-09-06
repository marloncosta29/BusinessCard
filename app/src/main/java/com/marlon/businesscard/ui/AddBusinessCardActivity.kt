package com.marlon.businesscard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marlon.businesscard.databinding.ActivityAddBusinessCardBinding

class AddBusinessCardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBusinessCardBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializeListeners()
    }

    private fun inicializeListeners () {
        binding.btnClose.setOnClickListener{
            finish()
        }
        binding.btnConfirm.setOnClickListener{

        }
    }

}