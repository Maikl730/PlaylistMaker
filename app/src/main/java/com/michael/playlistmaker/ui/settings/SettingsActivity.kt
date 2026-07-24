package com.michael.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.michael.playlistmaker.util.Creator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.ActivitySettingsBinding

const val THEME_PREFERENCES = "theme_preferences"
const val EDIT_THEME_KEY = "key_for_edit_theme"

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val themeSwitcherControlInteractor = Creator.provideThemeSwitcherControlInteractor()


        binding.toolBar.setNavigationOnClickListener{
            finish()
        }

        binding.switchTheme.isChecked =  themeSwitcherControlInteractor.getPosition()
        binding.switchTheme.setOnCheckedChangeListener { switcher, checked ->
            themeSwitcherControlInteractor.switchTheme(checked)
           // (applicationContext as App).switchTheme(checked)
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            setType("text/plain")
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

        binding.shareButton.setOnClickListener{
            val share = Intent.createChooser(shareIntent, null)
            startActivity(share)
        }

        binding.supportButton.setOnClickListener{
            startActivity(supportIntent)
        }

        binding.declarationButton.setOnClickListener{
            startActivity(declarationIntent)
        }
    }

}
