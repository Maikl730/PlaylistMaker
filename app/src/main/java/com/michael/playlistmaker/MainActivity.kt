package com.michael.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity,"Кнопка поиска",Toast.LENGTH_LONG).show()
            }
        }

        searchButton.setOnClickListener(searchClickListener)
        mediaButton.setOnClickListener { Toast.makeText(this,"Медиатека",Toast.LENGTH_LONG).show()}
        settingsButton.setOnClickListener { Toast.makeText(this,"Настройки",Toast.LENGTH_LONG).show() }
    }



}