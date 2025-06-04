package com.zack.rewards.sample.demoapp.ai

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.zack.rewards.sample.demoapp.databinding.FragmentGeminiBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.util.hideKeyboard
import kotlinx.coroutines.launch

/**
 *
 * @author zack.keng
 * Created on 2024/10/22
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
class GeminiImageFragment : InfoFragment() {
    private lateinit var binding: FragmentGeminiBinding
    private lateinit var geminiModel: GenerativeModel
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri: Uri? = null

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeminiBinding.inflate(inflater, container, false)
        binding.promptAddButton.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geminiModel = GoogleGenAI.newModel(GeminiModel.GEMINI_2_0_FLASH, 0.2f, 32, 1f, 4096)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("GeminiAI", "URI - $uri")
                binding.promptImage.setImageURI(uri)
                imageUri = uri
                binding.promptImageLayout.visibility = View.VISIBLE
            } else {
                Log.d("GeminiAI", "URI - null")
            }
        }
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo Gemini AI")
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    private fun setListener() {
        binding.promptSendButton.setOnClickListener {
            requireContext().hideKeyboard(binding.geminiInput)
            generateContent(
                binding.geminiInput.text.toString(),
                imageUri?.toBitmap(requireContext())
            )
        }
        binding.promptAddButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    private fun generateContent(prompt: String, bitmap: Bitmap?) {
        binding.geminiAnswerCard.visibility = View.VISIBLE
        var outputContent = ""
        binding.geminiAnswer.text = outputContent
        lifecycleScope.launch {
            geminiModel.generateContentStream(
                content {
                    bitmap?.let { image(it) }
                    text(prompt)
                }
            ).collect { response ->
                outputContent += response.text
                binding.geminiAnswer.text = outputContent
            }
        }
    }
}