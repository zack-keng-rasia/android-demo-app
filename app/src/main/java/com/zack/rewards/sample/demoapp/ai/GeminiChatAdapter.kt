package com.zack.rewards.sample.demoapp.ai

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zack.rewards.sample.demoapp.databinding.ViewChatReceiverBinding
import com.zack.rewards.sample.demoapp.databinding.ViewChatSenderBinding

/**
 *
 * @author zack.keng
 * Created on 2024/10/18
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
class GeminiChatAdapter(private var data: List<MessageItem>) :
    Adapter<GeminiChatAdapter.ChatViewHolder>() {
    companion object {
        const val SENDER = 0
        const val RECEIVER = 1
    }

    open inner class ChatViewHolder(itemView: View) : ViewHolder(itemView)

    inner class SenderViewHolder(val binding: ViewChatSenderBinding) :
        ChatViewHolder(binding.root) {
        fun bindData(message: String, bitmap: Bitmap?) {
            binding.chatSenderMsg.text = message
            bitmap?.let {
                binding.chatSenderImg.setImageBitmap(it)
            }

            binding.chatSenderImg.visibility = if (bitmap == null) View.GONE else View.VISIBLE
        }
    }

    inner class ReceiverViewHolder(val binding: ViewChatReceiverBinding) :
        ChatViewHolder(binding.root) {
        fun bindData(message: String) {
            binding.chatReceiverMsg.text = message
        }
    }

    fun addMessageItem(messageItem: MessageItem) {
        this.data += messageItem
        notifyItemInserted(data.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].role == Role.USER) {
            SENDER
        } else {
            RECEIVER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == SENDER) {
            val binding =
                ViewChatSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SenderViewHolder(binding)
        } else {
            val binding =
                ViewChatReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReceiverViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        if (getItemViewType(position) == SENDER) {
            (holder as SenderViewHolder).bindData(data[position].message, data[position].bitmap)
        } else {
            (holder as ReceiverViewHolder).bindData(data[position].message)
        }
    }
}