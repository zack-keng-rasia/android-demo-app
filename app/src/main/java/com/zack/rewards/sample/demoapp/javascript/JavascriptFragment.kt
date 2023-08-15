package com.zack.rewards.sample.demoapp.javascript

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zack.rewards.sample.demoapp.databinding.FragmentJavascriptBinding

/**
 *
 * @author zack.keng
 * Created on 2023/08/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class JavascriptFragment : Fragment() {
    private lateinit var binding: FragmentJavascriptBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJavascriptBinding.inflate(inflater, container, false)
        return binding.root
    }
}