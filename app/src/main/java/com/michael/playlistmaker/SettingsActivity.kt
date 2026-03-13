package com.michael.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        backButton.setNavigationOnClickListener{
            finish()
        }
        val shareButton = findViewById<TextView>(R.id.share_button)
        val supportButton = findViewById<TextView>(R.id.support_button)
        val declarationButton = findViewById<TextView>(R.id.declaration_button)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            setType("text/plain")
            data = Uri.parse("sms:")
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.link_to_practikum))
        }


        val supportIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.student_mail)))
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.message_to_support))
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.title_to_support))
        }


        val declarationIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(resources.getString(R.string.link_to_offerta))
        }

        shareButton.setOnClickListener{

            val share = Intent.createChooser(shareIntent, null)
            startActivity(share)
        }

        supportButton.setOnClickListener{
            startActivity(supportIntent)
        }

        declarationButton.setOnClickListener{
            startActivity(declarationIntent)
        }
    }

}