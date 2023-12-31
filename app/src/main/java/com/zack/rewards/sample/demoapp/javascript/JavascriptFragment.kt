package com.zack.rewards.sample.demoapp.javascript

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.zack.rewards.sample.demoapp.databinding.FragmentJavascriptBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment

/**
 *
 * @author zack.keng
 * Created on 2023/08/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class JavascriptFragment : InfoFragment(), CompoundButton.OnCheckedChangeListener {
    private lateinit var binding: FragmentJavascriptBinding
    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJavascriptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo the communication between Javascript and Native Code")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()

        binding.webView.loadUrl("file:///android_asset/index.html")
        binding.statusSwitch.setOnCheckedChangeListener(this)
    }

    private fun setSwitch(isOn: Boolean) {
        binding.statusSwitch.setOnCheckedChangeListener(null)
        binding.statusSwitch.isChecked = isOn
        binding.status.text = "Change switch flag from Javascript"
        binding.statusSwitch.setOnCheckedChangeListener(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        with(binding.webView) {
            settings.javaScriptEnabled = true
            addJavascriptInterface(SwitcherInterface {
                setSwitch(it)
            }, "RewardSDKDemo")
            addJavascriptInterface(WebAppInterface(requireContext()), "Android")
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        binding.status.text = "Switch changed to $isChecked"
    }
}