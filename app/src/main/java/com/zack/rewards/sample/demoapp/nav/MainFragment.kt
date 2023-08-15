package com.zack.rewards.sample.demoapp.nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import com.zack.rewards.sample.demoapp.R
import com.zack.rewards.sample.demoapp.databinding.FragmentMainBinding
import com.zack.rewards.sample.demoapp.util.showToast

/**
 *
 * @author zack.keng
 * Created on 2023/08/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class MainFragment : ListFragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var features: Array<String>

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
        features = resources.getStringArray(R.array.Features)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, features)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        when (val selectedFeature = features[position]) {
            "Javascript" -> {
                val directions = MainFragmentDirections.actionToJavascriptFragment()
                findNavController().navigate(directions)
            }

            else -> showToast("Feature [$selectedFeature] not implemented")
        }
    }
}