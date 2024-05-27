package com.zack.rewards.sample.demoapp.mission

import android.os.Bundle
import com.rakuten.gap.ads.mission_core.RakutenAuth
import com.rakuten.gap.ads.mission_core.activity.RakutenRewardBaseActivity
import com.rakuten.gap.ads.mission_core.api.status.RakutenRewardAPIError
import com.rakuten.gap.ads.mission_core.listeners.LoginResultCallback
import com.rakuten.gap.ads.mission_core.listeners.LogoutResultCallback
import com.rakuten.gap.ads.mission_core.status.RakutenRewardSDKStatus
import com.zack.rewards.sample.demoapp.databinding.ActivityMissionSdkBinding
import com.zack.rewards.sample.demoapp.util.toastMessage

/**
 *
 * @author zack.keng
 * Created on 2023/09/04
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class MissionSdkActivity : RakutenRewardBaseActivity() {
    private lateinit var binding: ActivityMissionSdkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissionSdkBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    override fun onSDKStatusChanged(status: RakutenRewardSDKStatus) {
        updateButtons(status == RakutenRewardSDKStatus.ONLINE)
    }

    private fun updateButtons(isLoggedIn: Boolean) {
        binding.missionLogin.isEnabled = !isLoggedIn
        binding.missionLogout.isEnabled = isLoggedIn
    }

    private fun setListener() {
        with(binding) {
            missionLogin.setOnClickListener {
                login()
            }
            missionLogout.setOnClickListener {
                logout()
            }
        }
    }

    private fun logout() {
        RakutenAuth.logout(object : LogoutResultCallback {
            override fun logoutFailed(e: RakutenRewardAPIError) {
                toastMessage("logout failed [$e]")
            }

            override fun logoutSuccess() {
                toastMessage("logout success")
            }

        })
    }

    private fun login() {
        RakutenAuth.openLoginPage(this) {
            if (it.resultCode == RESULT_OK) {
                RakutenAuth.handleActivityResult(it.data, object : LoginResultCallback {
                    override fun loginFailed(e: RakutenRewardAPIError) {
                        toastMessage("Login failed [$e]")
                    }

                    override fun loginSuccess() {
                        toastMessage("Login success")
                    }

                })
            }
        }
    }
}