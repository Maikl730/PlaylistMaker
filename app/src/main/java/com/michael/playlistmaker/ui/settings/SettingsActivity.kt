package com.michael.playlistmaker.ui.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.ActivitySettingsBinding
import com.michael.playlistmaker.databinding.FragmentSettingsBinding
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

const val THEME_PREFERENCES = "theme_preferences"
const val EDIT_THEME_KEY = "key_for_edit_theme"

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val themeSwitcherControlInteractor:ThemeSwitcherControlInteractor = getKoin().get()

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
