package com.zack.rewards.sample.demoapp.shortcut

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.navigation.fragment.navArgs
import com.zack.rewards.sample.demoapp.R
import com.zack.rewards.sample.demoapp.databinding.FragmentAppShortcutBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.nav.MainActivity
import com.zack.rewards.sample.demoapp.util.hideKeyboard
import com.zack.rewards.sample.demoapp.util.showLongToast

/**
 *
 * @author zack.keng
 * Created on 2023/08/23
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class ShortcutFragment : InfoFragment() {
    companion object {
        const val ACTION_OPEN_WEB = "com.zack.android.demo.OPEN_WEB"
    }

    private lateinit var binding: FragmentAppShortcutBinding

    private val args: ShortcutFragmentArgs by navArgs()

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppShortcutBinding.inflate(inflater, container, false)
        setIconStatus(false)
        return binding.root
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo app shortcut.\nPress bookmark icon to add or remove dynamic shortcut.\nPress add to home screen to add pinned shortcut.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pageUrl = args.pageUrl
        setUpWebView(pageUrl)
        setListener()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(defaultUrl: String?) {
        with(binding.shortcutWebView) {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
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
                    setIconStatus(false)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("Shortcut", "onPageFinished: $url")
                    binding.shortcutWebProgress.visibility = View.GONE
                    binding.shortcutEnterUrl.setText(url)
                    checkForDynamicShortcut(view?.title ?: "")
                    setIconStatus(true)
                }
            }
        }

        if (!defaultUrl.isNullOrEmpty()) {
            binding.shortcutEnterUrl.setText(defaultUrl)
            binding.shortcutWebView.loadUrl(defaultUrl)
        }
    }

    private fun setIconStatus(enable: Boolean) {
        binding.shortcutBookmark.isEnabled = enable
        binding.shortcutAddToHome.isEnabled = enable
    }

    private fun setListener() {
        with(binding) {
            shortcutEnterUrl.setOnEditorActionListener(editorActionListener)
            shortcutAddToHome.setOnClickListener {
                pinnedShortcut()
            }
        }
    }

    private fun checkForDynamicShortcut(title: String) {
        val shortcutId = "dyna_$title"
        Log.d("Shortcut", "Check for dynamic shortcut: $shortcutId")
        val shortcut = ShortcutManagerCompat.getDynamicShortcuts(requireContext()).find {
            it.id == shortcutId
        }
        if (shortcut == null) {
            Log.d("Shortcut", "Not found! User can add the shortcut")
            binding.shortcutBookmark.setImageResource(R.drawable.outline_bookmark_add_24)
            binding.shortcutBookmark.setOnClickListener {
                dynamicShortcut()
            }
        } else {
            Log.d("Shortcut", "Found! User can remove the shortcut")
            binding.shortcutBookmark.setImageResource(R.drawable.baseline_bookmark_remove_24)
            binding.shortcutBookmark.setOnClickListener {
                removeDynamicShort(shortcutId)
            }
        }
    }

    private fun removeDynamicShort(id: String) {
        ShortcutManagerCompat.removeDynamicShortcuts(requireContext(), listOf(id))
        showLongToast("Removed dynamic shortcut")
        Log.d("Shortcut", "Removed dynamic shortcut: $id")
        checkForDynamicShortcut(id.replace("dyna_", ""))
    }

    private fun createShortcutInfo(tag: String): ShortcutInfoCompat {
        val favicon = binding.shortcutWebView.favicon
        val icon = if (favicon != null) {
            IconCompat.createWithBitmap(favicon)
        } else {
            IconCompat.createWithResource(requireContext(), R.drawable.twotone_web_24)
        }
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            action = ACTION_OPEN_WEB
            data = Uri.parse(binding.shortcutWebView.url)
        }
        val title = binding.shortcutWebView.title ?: "UNKNOWN"

        return ShortcutInfoCompat.Builder(requireContext(), tag + "_$title")
            .setShortLabel(title)
            .setLongLabel("Open $title")
            .setIcon(icon)
            .setIntent(intent)
            .build()
    }

    private fun pinnedShortcut() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(requireContext())) {
            val shortcutInfo = createShortcutInfo("pinned")

            Log.d("Shortcut", "Creating pinned shortcut: ${shortcutInfo.id}")
            ShortcutManagerCompat.requestPinShortcut(requireContext(), shortcutInfo, null)
        } else {
            showLongToast("Device not supported for pin shortcut.")
        }
    }

    private fun dynamicShortcut() {


        val max = ShortcutManagerCompat.getMaxShortcutCountPerActivity(requireContext())


        val count = ShortcutManagerCompat.getDynamicShortcuts(requireContext()).count()

        Log.d("Shortcut", "Maximum shortcut [$max]")
        Log.d("Shortcut", "Current dynamic shortcut count [$count]")
        
        try {
            val shortcut = createShortcutInfo("dyna")

            Log.d("Shortcut", "Creating dynamic shortcut: ${shortcut.id}")
            ShortcutManagerCompat.pushDynamicShortcut(requireContext(), shortcut)

            checkForDynamicShortcut(shortcut.shortLabel.toString())
            showLongToast("Dynamic Shortcut created")
        } catch (e: Exception) {
            showLongToast("Already reached the maximum number of shortcuts!")
            Log.w("Shortcut", "Exceed maximum count already!", e)
        }
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