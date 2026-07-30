package com.michael.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.michael.playlistmaker.util.Creator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.ActivitySettingsBinding
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.presentation.search.TracksViewModel
import com.michael.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

const val THEME_PREFERENCES = "theme_preferences"
const val EDIT_THEME_KEY = "key_for_edit_theme"

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingsBinding
    //private var viewModel:SettingsViewModel? = null
    private val viewModel by viewModel<SettingsViewModel>()

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
        val themeSwitcherControlInteractor:ThemeSwitcherControlInteractor = getKoin().get()
/*
        viewModel = ViewModelProvider(this, SettingsViewModel.getFactory())
            .get(SettingsViewModel::class.java)

 */




        viewModel?.observeDoIntent()?.observe(this) {
            startActivity(it)
        }
        binding.toolBar.setNavigationOnClickListener{
            finish()
        }

        binding.switchTheme.isChecked =  themeSwitcherControlInteractor.getPosition()
        binding.switchTheme.setOnCheckedChangeListener { switcher, checked ->
            themeSwitcherControlInteractor.switchTheme(checked)
        }

        binding.shareButton.setOnClickListener{
            viewModel?.share()
        }

        binding.supportButton.setOnClickListener{
            viewModel?.support()
        }

        binding.declarationButton.setOnClickListener{
            viewModel?.declaration()
        }
    }

}
