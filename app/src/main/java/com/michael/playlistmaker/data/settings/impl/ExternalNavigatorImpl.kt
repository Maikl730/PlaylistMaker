package com.michael.playlistmaker.data.settings.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.settings.api.ExternalNavigator

class ExternalNavigatorImpl(var context: Context): ExternalNavigator {

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        setType("text/plain")
        putExtra(Intent.EXTRA_TEXT, context.resources.getString(R.string.link_to_practikum))
    }


    val supportIntent = Intent().apply {
        action = Intent.ACTION_SENDTO
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.resources.getString(R.string.student_mail)))
        putExtra(Intent.EXTRA_TEXT, context.resources.getString(R.string.message_to_support))
        putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.title_to_support))
    }


    val declarationIntent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(context.resources.getString(R.string.link_to_offerta))
    }

    override fun openSupport():Intent {
        return supportIntent

    }

    override fun openTerms(): Intent {
        return declarationIntent
    }

    override fun shareApp():Intent {
        val share = Intent.createChooser(shareIntent, null)
        return share
    }
}
