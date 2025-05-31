package com.nahyun.mz.ui.discussion

import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityDiscussionDetailBinding
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.ui.base.BaseActivity
import com.nahyun.mz.ui.discussion.DiscussionDetailViewModel.Companion.USER_ID
import com.nahyun.mz.ui.discussion.DiscussionFragment.Companion.POST_KEY
import com.nahyun.mz.ui.discussion.adapter.DiscussionCommentAdapter

class DiscussionDetailActivity : BaseActivity<ActivityDiscussionDetailBinding>(R.layout.activity_discussion_detail) {

    private lateinit var commentAdapter: DiscussionCommentAdapter
    private val viewModel: DiscussionDetailViewModel by viewModels()

    override fun setup() {
        viewModel.initPost(intent.getSerializableExtra(POST_KEY) as Post)
        viewModel.fetchData()
        initClickListeners()
        setAdapter()
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            viewModel = this@DiscussionDetailActivity.viewModel
            lifecycleOwner = this@DiscussionDetailActivity
        }

        initObserves()
    }

    private fun initClickListeners() {
        // 뒤로가기
        binding.discussionDetailBackIv.setOnClickListener {
            finish()
        }

        // 좋아요
        binding.postLikeTv.setOnClickListener {
            val post = viewModel.post.value!!
            if (post.authorId == USER_ID) { // 게시글 작성자가 나일 때
                Toast.makeText(this@DiscussionDetailActivity, "내가 쓴 댓글은 공감할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!post.isLike) {
                Toast.makeText(this@DiscussionDetailActivity, "이 글을 공감하였습니다.", Toast.LENGTH_SHORT).show()
                viewModel.togglePostLike()
            } else {
                Toast.makeText(this@DiscussionDetailActivity, "이미 공감한 글입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter() {
        commentAdapter = DiscussionCommentAdapter(viewModel.post.value!!.authorId)
        binding.postCommentRv.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context)
        }
        commentAdapter.setCommentClickListener(object: DiscussionCommentAdapter.MyItemClickListener {
            override fun onLikeClick(position: Int) {
                val comment = viewModel.commentList.value!![position]
                if (comment.userId == USER_ID) { // 작성자가 나일 때
                    Toast.makeText(this@DiscussionDetailActivity, "내가 쓴 댓글은 공감할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                if (!comment.isLike) {
                    Toast.makeText(this@DiscussionDetailActivity, "이 댓글을 공감하였습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.toggleCommentLike(position)
                } else {
                    Toast.makeText(this@DiscussionDetailActivity, "이미 공감한 댓글입니다.", Toast.LENGTH_SHORT).show()
                    return
                }
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

        viewModel.postSuccess.observe(this) { isSuccess ->
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.commentInputEt.windowToken, 0)
    }
}