package com.nahyun.mz.ui.translator

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nahyun.mz.MZApplication
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentTranslatorBinding
import com.nahyun.mz.ui.base.BaseFragment

class TranslatorFragment : BaseFragment<FragmentTranslatorBinding>(R.layout.fragment_translator) {
    private val viewModel: TranslatorViewModel by activityViewModels {
        TranslatorViewModelFactory((requireActivity().application as MZApplication).repository)
    }

    override fun setup() {
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // 사전 보기 버튼 클릭 리스너
        binding.btnDictionary.setOnClickListener {
            findNavController().navigate(R.id.action_translatorFragment_to_dictionaryFragment)
        }

        // 검색하기(번역) 버튼 클릭 리스너
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearchWord.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchWord(query)
            }
        }

        // 즐겨찾기 버튼 클릭 리스너
        binding.cardResult.ivStar.setOnClickListener {
            val currentWord = viewModel.searchResult.value
            if (currentWord == null) return@setOnClickListener
            viewModel.toggleSearchWordIsLike() // 즐겨찾기/해제
        }

        // 즐겨찾는 목록 보기 버튼 클릭 리스너
        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_translatorFragment_to_favoriteWordsFragment)
        }
    }

    private fun observeViewModel() {
        // 검색 결과 관찰
        viewModel.searchResult.observe(this) { result ->
            binding.resultWord = result
            if (result == null) {
                Toast.makeText(requireActivity(), "번역 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}