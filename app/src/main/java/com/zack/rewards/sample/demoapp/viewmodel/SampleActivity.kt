package com.zack.rewards.sample.demoapp.viewmodel

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * @author zack.keng
 * Created on 2023/09/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class SampleActivity: AppCompatActivity() {

    val viewModel: SampleViewModel by viewModels()
}