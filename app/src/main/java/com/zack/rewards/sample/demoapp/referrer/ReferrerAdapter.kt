package com.zack.rewards.sample.demoapp.referrer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.zack.rewards.sample.demoapp.databinding.LayoutReferrerItemBinding

/**
 *
 * @author zack.keng
 * Created on 2023/08/18
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class ReferrerAdapter(
    context: Context,
    resource: Int,
    private val referrerData: MutableList<ReferrerData>
) :
    ArrayAdapter<ReferrerData>(context, resource, referrerData) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = LayoutReferrerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val data = referrerData[position]
        binding.referrerLabel.text = data.label
        binding.referrerValue.text = data.referrerValue.orEmpty()
        return binding.root
    }
}