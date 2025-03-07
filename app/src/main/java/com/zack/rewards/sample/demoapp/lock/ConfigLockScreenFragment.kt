package com.zack.rewards.sample.demoapp.lock

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zack.rewards.sample.demoapp.databinding.FragmentLockscreenConfigBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.util.Pref

/**
 *
 * @author zack.keng
 * Created on 2025/03/07
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class ConfigLockScreenFragment : InfoFragment() {
    private lateinit var binding: FragmentLockscreenConfigBinding

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockscreenConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to test the lock screen behaviour")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            var lockScreen = Pref.INSTANCE.getLockScreenEnabled()
            if (lockScreen && !Settings.canDrawOverlays(requireContext())) {
                Pref.INSTANCE.setLockScreenEnabled(false)
                lockScreen = false
            }
            val timing = Pref.INSTANCE.getLockScreenTiming()
            switchShowLockScreen.isChecked = lockScreen

            showRadioGroup(lockScreen)
            if (timing == Pref.LOCK_SCREEN_TIMING_OFF) {
                radioScreenOff.isChecked = true
            } else {
                radioScreenOn.isChecked = true
            }
            setSwitchListener()

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    radioScreenOff.id -> Pref.INSTANCE.setLockScreenTiming(Pref.LOCK_SCREEN_TIMING_OFF)
                    radioScreenOn.id -> Pref.INSTANCE.setLockScreenTiming(Pref.LOCK_SCREEN_TIMING_ON)
                }
            }
        }
    }

    private fun showRadioGroup(show: Boolean) {
        binding.radioGroup.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${requireContext().packageName}")
            )
            startActivityForResult(intent, 1234)
        } else {
            showRadioGroup(true)
            Pref.INSTANCE.setLockScreenEnabled(true)
        }
    }

    private fun setSwitchListener() {
        binding.switchShowLockScreen.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkOverlayPermission()
            } else {
                Pref.INSTANCE.setLockScreenEnabled(false)
                showRadioGroup(false)
           }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234) {
            if (Settings.canDrawOverlays(requireContext())) {
                showRadioGroup(true)
                Pref.INSTANCE.setLockScreenEnabled(true)
            } else {
                binding.switchShowLockScreen.setOnCheckedChangeListener(null)
                binding.switchShowLockScreen.isChecked = false
                setSwitchListener()
            }
        }
    }
}