package com.zack.rewards.sample.demoapp.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.zack.rewards.sample.demoapp.databinding.FragmentGeminiBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import com.zack.rewards.sample.demoapp.util.hideKeyboard
import com.zack.rewards.sample.demoapp.util.showToast
import kotlinx.coroutines.launch

/**
 *
 * @author zack.keng
 * Created on 2024/10/17
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
class GeminiAiFragment : InfoFragment() {
    private lateinit var binding: FragmentGeminiBinding
    private lateinit var geminiModel: GenerativeModel
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeminiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geminiModel = GoogleGenAI.newModel(GeminiModel.GEMINI_1_0_PRO, 0.2f, 32, 1f, 4096)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("GeminiAI", "URI - $uri")
                val bitmap = uri.toBitmap(requireContext())
                val prompt = binding.geminiInput.text.toString()
                if (prompt.isEmpty()) {
                    showToast("Please enter a prompt")
                } else {
                    generateContent(bitmap, prompt)
                }
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

    private fun generateContent(bitmap: Bitmap, prompt: String) {
        lifecycleScope.launch {
            val response = geminiModel.generateContent(
                content {
                    image(bitmap)
                    text(prompt)
                }
            )
            binding.geminiAnswer.text = response.text
            binding.geminiAnswerCard.visibility = View.VISIBLE
        }
    }

    private fun generateContent(prompt: String) {
        binding.geminiAnswerCard.visibility = View.VISIBLE
        binding.geminiAnswer.text = ""
        lifecycleScope.launch {
            var outputContent = ""
            geminiModel.generateContentStream(prompt).collect { response ->
                outputContent += response.text
                binding.geminiAnswer.text = outputContent
            }
        }
    }

    private fun setListener() {
        binding.promptSendButton.setOnClickListener {
            requireContext().hideKeyboard(binding.geminiInput)
            generateContent(binding.geminiInput.text.toString())
//            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
}

fun Uri.toBitmap(context: Context): Bitmap {
    context.contentResolver.openInputStream(this).use { inputStream ->
        return Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream))
    }
}
