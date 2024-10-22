package com.zack.rewards.sample.demoapp.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.zack.rewards.sample.demoapp.databinding.FragmentMainBinding
import com.zack.rewards.sample.demoapp.nav.FeatureItem

/**
 *
 * @author zack.keng
 * Created on 2024/10/22
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
class GeminiApiFragment : ListFragment() {
    private lateinit var binding: FragmentMainBinding
    private val features = listOf(
        FeatureItem("Text Generation", GeminiApiFragmentDirections.goToGeminiAiFragment()),
        FeatureItem("Image Prompting", GeminiApiFragmentDirections.goToGeminiImageFragment()),
        FeatureItem("Multi-turn Chat", GeminiApiFragmentDirections.goToGeminiChatFragment())
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val labels = features.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, labels)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val selectedFeature = features[position]
        navigate(selectedFeature.directions)
    }

    private fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }
}