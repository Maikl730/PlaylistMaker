package com.michael.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.playlistmaker.databinding.FragmentSettingsBinding
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

class SettingFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themeSwitcherControlInteractor: ThemeSwitcherControlInteractor = getKoin().get()

        viewModel?.observeDoIntent()?.observe(viewLifecycleOwner) {
            startActivity(it!!)
        }
        binding.toolBar.setNavigationOnClickListener{
           // finish()
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