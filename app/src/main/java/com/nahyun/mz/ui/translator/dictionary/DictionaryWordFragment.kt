package com.nahyun.mz.ui.translator.dictionary

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahyun.mz.MZApplication
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentDictionaryWordBinding
import com.nahyun.mz.domain.model.WordType
import com.nahyun.mz.domain.model.WordType.RECENT
import com.nahyun.mz.ui.base.BaseFragment

class DictionaryWordFragment: BaseFragment<FragmentDictionaryWordBinding>(R.layout.fragment_dictionary_word) {

    private lateinit var adapter: DictionaryWordRVAdapter
    private var fragmentType: WordType = RECENT

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory((requireActivity().application as MZApplication).repository)
    }

    override fun setup() {
        setAdapter()
        initObserves()
    }

    override fun onStart() {
        super.onStart()

        viewModel.getAllWords()
    }

    private fun setAdapter() {
        adapter = DictionaryWordRVAdapter { word ->
            viewModel.updateWordLike(word.id, word.isLike)
        }

        binding.rvWord.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@DictionaryWordFragment.adapter
        }
    }

    private fun initObserves() {
        viewModel.wordList.observe(this) { words ->
            val filteredData = viewModel.wordList.value!!.filter {
                it.type == fragmentType
            }
            // 데이터 추가
            adapter.submitList(filteredData)
        }
    }

    companion object {
        fun newInstance(wordType: WordType): DictionaryWordFragment {
            val fragment = DictionaryWordFragment()
            fragment.fragmentType = wordType
            return fragment
        }
    }
}