package com.zack.rewards.sample.demoapp.nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.zack.rewards.sample.demoapp.databinding.FragmentMainBinding

/**
 *
 * @author zack.keng
 * Created on 2023/08/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class MainFragment : ListFragment() {
    private lateinit var binding: FragmentMainBinding
    private val features = listOf(
        FeatureItem("Javascript", MainFragmentDirections.goToJavascriptFragment()),
        FeatureItem("InstallReferrer", MainFragmentDirections.goToReferrerFragment()),
        FeatureItem("App Shortcut", MainFragmentDirections.goToShortcutFragment()),
        FeatureItem("Memory Leak", MainFragmentDirections.goToLeakActivity())
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MainFragment", "onCreateView")
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

data class FeatureItem(
    val name: String,
    val directions: NavDirections
)