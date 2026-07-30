package com.michael.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.ActivityMainBinding
import com.michael.playlistmaker.presentation.main.MainViewModel
import com.michael.playlistmaker.presentation.settings.SettingsViewModel
import com.michael.playlistmaker.ui.mediateka.MediatekaActivity
import com.michael.playlistmaker.ui.search.SearchActivity
import com.michael.playlistmaker.ui.settings.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()
   // private var viewModel: MainViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, systemBars.top, v.paddingRight, systemBars.bottom)
            insets
        }
/*
        viewModel = ViewModelProvider(this, MainViewModel.getFactory())
            .get(MainViewModel::class.java)

 */




        viewModel?.observeDoIntent()?.observe(this) {
            startActivity(it)
        }

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
               viewModel?.search()
            }
        }

        binding.searchButton.setOnClickListener(searchClickListener)
        binding.mediaButton.setOnClickListener {viewModel?.mediateka()
           }
        binding.settingsButton.setOnClickListener {viewModel?.settings()
            }
    }



}