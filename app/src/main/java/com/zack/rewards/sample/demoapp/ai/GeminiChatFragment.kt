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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ai.Chat
import com.google.firebase.ai.type.content
import com.zack.rewards.sample.demoapp.databinding.FragmentGeminiChatBinding
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import kotlinx.coroutines.launch

/**
 *
 * @author zack.keng
 * Created on 2024/10/18
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
class GeminiChatFragment : InfoFragment() {
    private lateinit var binding: FragmentGeminiChatBinding
    private lateinit var adapter: GeminiChatAdapter
    private lateinit var geminiChat: Chat
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private var imageUri: Uri? = null

    override fun createFragmentView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeminiChatBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatList = mutableListOf(
            MessageItem(
                "Hi, I'm your personal AI assistant. Please tell me what is my role and how can I assist you.",
                Role.MODEL
            )
        )
        adapter = GeminiChatAdapter(chatList)
        geminiChat =
            GoogleGenAI.newModel(GeminiModel.GEMINI_2_0_FLASH, 0.2f, 32, 1f, 4096).startChat(
                history = chatList.map { content(it.role.role) { text(it.message) } },
            )
        binding.chatMessageList.adapter = adapter
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.chatImage.setImageURI(uri)
                binding.chatImageLayout.visibility = View.VISIBLE
            } else {
                Log.d("GeminiAI", "URI - null")
            }
        }
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo chat with Gemini AI.\nPress send to chat with Gemini AI.")
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    private fun setListener() {
        with(binding) {
            chatSendButton.setOnClickListener {
                val message = chatInput.text.toString()
                if (message.isNotEmpty()) {
                    sendMessage(message, imageUri?.toBitmap(requireContext()))
                    chatInput.text.clear()
                    chatImageLayout.visibility = View.GONE
                    imageUri = null
                }
            }
            chatInput.addTextChangedListener {
                chatSendButton.isEnabled = it.toString().isNotEmpty()
            }
            chatInput.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null &&
                            event.keyCode == KeyEvent.KEYCODE_ENTER &&
                            event.action == KeyEvent.ACTION_DOWN)
                ) {
                    chatSendButton.performClick()
                    true
                } else {
                    false
                }
            }
            chatAddButton.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun toggleChat(enable: Boolean) {
        binding.chatInput.isEnabled = enable
        binding.chatSendButton.isEnabled = enable
        binding.chatAddButton.isEnabled = enable
    }

    private fun addMessage(messageItem: MessageItem) {
        adapter.addMessageItem(messageItem)
        binding.chatMessageList.scrollToPosition(adapter.itemCount - 1)
    }

    private fun sendMessage(message: String, bitmap: Bitmap? = null) {
        addMessage(MessageItem(message, Role.USER, bitmap))
        toggleChat(false)
        lifecycleScope.launch {
            val response = if (bitmap != null) {
                geminiChat.sendMessage(
                    content {
                        image(bitmap)
                        text(message)
                    }
                )
            } else {
                geminiChat.sendMessage(message)
            }
            addMessage(MessageItem(response.text.toString(), Role.MODEL))
            toggleChat(true)
        }
    }
}