package com.nahyun.mz.ui.discussion

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentDiscussionBinding
import com.nahyun.mz.ui.base.BaseFragment
import com.nahyun.mz.ui.discussion.adapter.DiscussionPostAdapter

class DiscussionFragment : BaseFragment<FragmentDiscussionBinding>(R.layout.fragment_discussion) {

    private lateinit var postAdapter: DiscussionPostAdapter
    private val viewModel: DiscussionViewModel by viewModels()

    override fun setup() {
        binding.apply {
            viewModel = this@DiscussionFragment.viewModel
            lifecycleOwner = this@DiscussionFragment
        }
        setAdapter()
    }

    private fun setAdapter() {
        postAdapter = DiscussionPostAdapter()
        binding.discussionRv.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)
        }
        initObserves()
        postAdapter.setPostClickListener(object: DiscussionPostAdapter.MyItemClickListener {
            override fun onItemClick(position: Int) { // 아이템 전체 클릭
                val intent = Intent(requireActivity(), DiscussionDetailActivity::class.java)
                    .putExtra(
                        POST_KEY, viewModel.postList.value!![position]
                    )
                startActivity(intent)
            }
        })
    }

    private fun initObserves() {
//        postAdapter.addPost(viewModel.postList.value ?: emptyList())
        // postList를 관찰하여 리사이클러뷰 아이템에 추가
        viewModel.postList.observe(viewLifecycleOwner) { postList ->
            Log.d("DiscussionFg", "postList: $postList")
            if (!postList.isNullOrEmpty()) {
                postAdapter.addPost(postList)
            }
        }
    }

    companion object {
        const val POST_KEY = "post_key"
    }
}