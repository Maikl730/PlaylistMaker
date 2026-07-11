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

const val THEME_PREFERENCES = "theme_preferences"
const val EDIT_THEME_KEY = "key_for_edit_theme"

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
        val themeSwitcherControlInteractor = Creator.provideThemeSwitcherControlInteractor()

        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        backButton.setNavigationOnClickListener{
            finish()
        }

        val themeSwitcher = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(
            R.id.switch_theme
        )
        val shareButton = findViewById<TextView>(R.id.share_button)
        val supportButton = findViewById<TextView>(R.id.support_button)
        val declarationButton = findViewById<TextView>(R.id.declaration_button)

        themeSwitcher.isChecked =  themeSwitcherControlInteractor.getPosition()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
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
