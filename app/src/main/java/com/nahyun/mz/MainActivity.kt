package com.nahyun.mz

import com.nahyun.mz.databinding.ActivityMainBinding
import com.nahyun.mz.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    val name = "Mobile Programming"

    override fun setup() {
        binding.name = name
    }
}