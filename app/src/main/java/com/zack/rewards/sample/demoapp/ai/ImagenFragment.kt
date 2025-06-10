package com.zack.rewards.sample.demoapp.ai

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ai.ImagenModel
import com.google.firebase.ai.type.PublicPreviewAPI
import com.zack.rewards.sample.demoapp.databinding.FragmentImagenBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.util.hideKeyboard
import kotlinx.coroutines.launch

/**
 *
 * @author zack.keng
 * Created on 2025/06/04
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
@OptIn(PublicPreviewAPI::class)
class ImagenFragment : InfoFragment() {
    private lateinit var binding: FragmentImagenBinding
    private lateinit var imagenModel: ImagenModel

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagenModel = GoogleGenAI.imagenModel()
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo Imagen AI model")
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    private fun setListener() {
        binding.promptSendButton.setOnClickListener {
            requireContext().hideKeyboard(binding.geminiInput)
            generatingImage()
            generateImage(binding.geminiInput.text.toString())
        }

        binding.geminiInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_DOWN)
            ) {
                binding.promptSendButton.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun generateImage(prompt: String) {
        lifecycleScope.launch {
            val imageResponse = imagenModel.generateImages(prompt)
            val image = imageResponse.images.first()

            val bitmapImage = image.asBitmap()
            binding.generativeImage.setImageBitmap(bitmapImage)
            imageGenerated()
        }
    }

    private fun generatingImage() {
        binding.generativeProgress.visibility = View.VISIBLE
        binding.geminiInput.isEnabled = false
        binding.promptSendButton.isEnabled = false
    }

    private fun imageGenerated() {
        binding.generativeProgress.visibility = View.GONE
        binding.geminiInput.isEnabled = true
        binding.promptSendButton.isEnabled = true
    }
}