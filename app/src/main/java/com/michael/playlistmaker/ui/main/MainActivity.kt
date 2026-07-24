package com.michael.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.ActivityMainBinding
import com.michael.playlistmaker.ui.mediateka.MediatekaActivity
import com.michael.playlistmaker.ui.search.SearchActivity
import com.michael.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding



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

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }

        binding.searchButton.setOnClickListener(searchClickListener)
        binding.mediaButton.setOnClickListener {
            val mediaIntent = Intent(this@MainActivity, MediatekaActivity::class.java)
            startActivity(mediaIntent)}
        binding.settingsButton.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent) }
    }



}