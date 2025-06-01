package com.nahyun.mz.ui.translator

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

    private var isFavorite = false

    override fun setup() {
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // 검색하기 버튼 클릭 리스너
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearchWord.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchWord(query)
            }
        }

        // 즐겨찾기 버튼 클릭 리스너
        binding.ivStar.setOnClickListener {
            val currentWord = viewModel.searchResult.value
            if (currentWord != null) {
                if (isFavorite) { // 즐겨찾기 해제
                    viewModel.removeFromFavorites(currentWord.id)
                } else { // 즐겨찾기
                    viewModel.addToFavorites(currentWord.id)
                }
                isFavorite = !isFavorite
                updateFavoriteIcon()
            }
        }

        // 즐겨찾는 목록 보기 버튼 클릭 리스너
        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_translatorFragment_to_favoriteWordsFragment)
        }
    }

    private fun observeViewModel() {
        // 검색 결과 관찰
        viewModel.searchResult.observe(this) { result ->
            if (result != null) {
                binding.tvWordTitle.text = result.word
                binding.tvWordMeaning.text = result.meaning

                // 즐겨찾기 상태 확인 및 아이콘 업데이트
                viewModel.checkIsFavorite()
            }
        }

        // 즐겨찾기 상태 관찰
        viewModel.isFavorite.observe(this) { favorite ->
            isFavorite = favorite
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        binding.ivStar.setImageResource(
            if (isFavorite) R.drawable.ic_star_filled
            else R.drawable.ic_star_outline
        )
    }
}