package com.nahyun.mz.ui.translator.dictionary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nahyun.mz.domain.model.WordType

class DictionaryVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> DictionaryWordFragment.newInstance(WordType.RECENT) // 신조어
            else -> DictionaryWordFragment.newInstance(WordType.OLD) // 옛말
        }
    }
}