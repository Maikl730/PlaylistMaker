package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.FragmentMediatekaBinding
import com.michael.playlistmaker.databinding.FragmentSearchBinding
import com.michael.playlistmaker.presentation.mediateka.MediatekaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaFragment:Fragment() {
    private lateinit var binding: FragmentMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator

    private val viewModel by viewModel<MediatekaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.toolBar.setNavigationOnClickListener {
           // finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}