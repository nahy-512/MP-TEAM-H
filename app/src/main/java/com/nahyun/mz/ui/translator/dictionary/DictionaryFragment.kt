package com.nahyun.mz.ui.translator.dictionary

import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentDictionaryBinding
import com.nahyun.mz.ui.base.BaseFragment

class DictionaryFragment: BaseFragment<FragmentDictionaryBinding>(R.layout.fragment_dictionary) {
    override fun setup() {
        initClickListeners()
        setVPAdapter()
    }

    private fun initClickListeners() {
        // 뒤로가기
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setVPAdapter() {
        // 뷰페이저 설정
        val wordAdapter = DictionaryVPAdapter(this)
        binding.vpDictionary.adapter = wordAdapter

        // 탭 레이아웃을 뷰페이저와 동기화
        TabLayoutMediator(binding.tbDictionary, binding.vpDictionary) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    companion object {
        private val tabTitles = listOf("신조어", "옛말")
    }
}