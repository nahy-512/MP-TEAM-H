package com.nahyun.mz.ui

import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityMainBinding
import com.nahyun.mz.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun setup() {
        initNavigation()
    }

    private fun initNavigation() {
        NavigationUI.setupWithNavController(binding.mainNavBar, findNavController(R.id.main_nav_host))
    }
}