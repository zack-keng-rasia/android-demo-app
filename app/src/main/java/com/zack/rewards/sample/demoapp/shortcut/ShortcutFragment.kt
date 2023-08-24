package com.zack.rewards.sample.demoapp.shortcut

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView.OnEditorActionListener
import com.zack.rewards.sample.demoapp.databinding.FragmentAppShortcutBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.util.hideKeyboard
import java.util.logging.Logger

/**
 *
 * @author zack.keng
 * Created on 2023/08/23
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class ShortcutFragment : InfoFragment() {
    private lateinit var binding: FragmentAppShortcutBinding

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppShortcutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo app shortcut.\nPress bookmark icon to add or remove dynamic shortcut.\nPress add to home screen to add pinned shortcut.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
        setListener()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        with(binding.shortcutWebView) {
            settings.javaScriptEnabled = true
            webChromeClient = object: WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.shortcutWebProgress.progress = newProgress
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("Shortcut", "onPageStarted: $url")
                    binding.shortcutWebProgress.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("Shortcut", "onPageFinished: $url")
                    binding.shortcutWebProgress.visibility = View.GONE
                    binding.shortcutEnterUrl.setText(url)
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            shortcutEnterUrl.setOnEditorActionListener(editorActionListener)
            shortcutBookmark.setOnClickListener {
                dynamicShortcut()
            }
            shortcutAddToHome.setOnClickListener {
                pinnedShortcut()
            }
        }
    }

    private fun pinnedShortcut() {
        TODO("Not yet implemented")
    }

    private fun dynamicShortcut() {
        TODO("Not yet implemented")
    }

    private fun loadUrl(url: String) {
        Log.d("Shortcut", "LoadURL: $url")
        if (url.isNotEmpty()) {
            binding.shortcutWebView.loadUrl(if (!url.startsWith("http")) "https://$url" else url)
        }
    }

    private val editorActionListener = OnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
            requireContext().hideKeyboard(v)
            loadUrl(binding.shortcutEnterUrl.text.toString())
            return@OnEditorActionListener true
        }

        return@OnEditorActionListener false
    }

}