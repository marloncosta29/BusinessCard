package com.marlon.businesscard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.marlon.businesscard.App
import com.marlon.businesscard.R
import com.marlon.businesscard.databinding.ActivityMainBinding
import com.marlon.businesscard.util.Image

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy {
        BusinessCardAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rvCards.adapter = adapter
        getAllBusinessCard()
        inicializeListeners()
    }

    private fun inicializeListeners(){
        binding.fbtnAdd.setOnClickListener{
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java )
            startActivity(intent)
        }
        adapter.listenerShare = { card ->
            Image.share(this@MainActivity, card)
        }

    }

    private fun getAllBusinessCard(){
        mainViewModel.getAll().observe(this, { businessCard ->
            adapter.submitList(businessCard)
        })
    }

}