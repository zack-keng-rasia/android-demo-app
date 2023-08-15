package com.zack.rewards.sample.demoapp.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 *
 * @author zack.keng
 * Created on 2023/08/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLongToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Context.toastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}