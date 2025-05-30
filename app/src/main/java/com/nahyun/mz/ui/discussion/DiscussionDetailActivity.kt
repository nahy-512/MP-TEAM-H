package com.nahyun.mz.ui.discussion

import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityDiscussionDetailBinding
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.ui.base.BaseActivity
import com.nahyun.mz.ui.discussion.DiscussionFragment.Companion.POST_KEY
import com.nahyun.mz.ui.discussion.adapter.DiscussionCommentAdapter

class DiscussionDetailActivity : BaseActivity<ActivityDiscussionDetailBinding>(R.layout.activity_discussion_detail) {

    private lateinit var commentAdapter: DiscussionCommentAdapter
    private val viewModel: DiscussionDetailViewModel by viewModels()

    override fun setup() {
        viewModel.post = intent.getSerializableExtra(POST_KEY) as Post
        viewModel.fetchData()
        initClickListeners()
        setAdapter()
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            post = viewModel.post
            lifecycleOwner = this@DiscussionDetailActivity
        }
        initObserves()
    }

    private fun initClickListeners() {
        binding.discussionDetailBackIv.setOnClickListener {
            finish()
        }
    }

    private fun setAdapter() {
        commentAdapter = DiscussionCommentAdapter()
        binding.postCommentRv.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context)
        }
        commentAdapter.setCommentClickListener(object: DiscussionCommentAdapter.MyItemClickListener {
            override fun onLikeClick(routeId: Int) {
                Toast.makeText(this@DiscussionDetailActivity, "이 댓글을 공감하였습니다.", Toast.LENGTH_SHORT).show()
                //TODO: 댓글 좋아요 개수 증가
            }
        })
    }

    private fun initObserves() {
        viewModel.userList.observe(this) {
            if (it.isNotEmpty()) {
                binding.author = viewModel.getAuthorProfile()
            }
        }

        viewModel.commentList.observe(this) {
            if (it.isNotEmpty()) {
                commentAdapter.addComment(it)
            }
        }
    }
}