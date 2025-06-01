package com.nahyun.mz.ui.translator

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahyun.mz.MZApplication
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentFavoriteWordsBinding
import com.nahyun.mz.ui.base.BaseFragment

class FavoriteWordsFragment : BaseFragment<FragmentFavoriteWordsBinding>(R.layout.fragment_favorite_words) {

    private val viewModel: TranslatorViewModel by activityViewModels {
        TranslatorViewModelFactory((requireActivity().application as MZApplication).repository)
    }

    private lateinit var adapter: FavoriteWordsAdapter

    override fun setup() {
        binding.viewModel = viewModel

        initClickListeners()
        setupRecyclerView()
        observeViewModel()
    }

    private fun initClickListeners() {
        // 뒤로가기
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 모두 삭제
        binding.tvDeleteAll.setOnClickListener {
            viewModel.removeAllFavorites()
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteWordsAdapter { word ->
            // 즐겨찾기 삭제 기능
            viewModel.removeFromFavorites(word.id)
        }

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteWordsFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.favorites.observe(this) { favorites ->
            adapter.submitList(favorites)
        }
    }
}