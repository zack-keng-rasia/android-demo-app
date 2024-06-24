package com.zack.rewards.sample.demoapp.referrer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse
import com.android.installreferrer.api.InstallReferrerStateListener
import com.zack.rewards.sample.demoapp.R
import com.zack.rewards.sample.demoapp.databinding.FragmentReferrerBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.util.showLongToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 *
 * @author zack.keng
 * Created on 2023/08/18
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class ReferrerFragment : InfoFragment() {

    private lateinit var binding: FragmentReferrerBinding
    private lateinit var referrerClient: InstallReferrerClient

    private val referrerData = mutableListOf<ReferrerData>()
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US)
    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReferrerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo the Google Install Referrer API")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callReferrerApi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        referrerClient.endConnection()
    }

    private fun callReferrerApi() {
        referrerClient = InstallReferrerClient.newBuilder(requireContext()).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                referrerData.clear()
                when (responseCode) {
                    InstallReferrerResponse.OK -> {
                        val response = referrerClient.installReferrer

                        referrerData.addItem("Install URL", response.installReferrer)
                        referrerData.addItem("Install Version", response.installVersion)
                        referrerData.addItem(
                            "Click Time",
                            getTimeStamp(response.referrerClickTimestampSeconds)
                        )
                        referrerData.addItem(
                            "Click Server Time",
                            getTimeStamp(response.referrerClickTimestampServerSeconds)
                        )
                        referrerData.addItem(
                            "App Install Time",
                            getTimeStamp(response.installBeginTimestampSeconds)
                        )
                        referrerData.addItem(
                            "App Install Server Time",
                            getTimeStamp(response.installBeginTimestampServerSeconds)
                        )
                        referrerData.addItem("Instant Play", "${response.googlePlayInstantParam}")
                    }

                    InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> referrerData.addItem(
                        "Response Code",
                        "$responseCode: Feature not supported"
                    )

                    InstallReferrerResponse.SERVICE_UNAVAILABLE -> referrerData.addItem(
                        "Response Code",
                        "$responseCode: Failed to establish connection"
                    )

                    else -> {
                        referrerData.addItem("Response Code", "$responseCode: Not recognize code")
                    }
                }
                setAdapter()
            }

            override fun onInstallReferrerServiceDisconnected() {
                showLongToast("Service Disconnected")
            }

        })
    }

    private fun MutableList<ReferrerData>.addItem(label: String, value: String?) {
        this.add(ReferrerData(label, value))
    }

    private fun setAdapter() {
        val adapter = ReferrerAdapter(requireContext(), R.layout.layout_referrer_item, referrerData)
        binding.referrerDetails.adapter = adapter
    }

    private fun getTimeStamp(seconds: Long): String {
        if (seconds == 0L) return "NA"
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (seconds*1000)
        return try {
            sdf.format(calendar.timeInMillis)
        } catch (e: Exception) {
            Log.e("InstallReferrerPage", "Format Date exception: $seconds", e)
            "NA"
        }
    }
}