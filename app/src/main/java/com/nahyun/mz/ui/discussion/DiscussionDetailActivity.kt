package com.nahyun.mz.ui.discussion

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahyun.mz.MZApplication
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityDiscussionDetailBinding
import com.nahyun.mz.databinding.ItemTooltipBinding
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.domain.model.Word
import com.nahyun.mz.domain.repository.DiscussionRepository.Companion.USER_ID
import com.nahyun.mz.ui.base.BaseActivity
import com.nahyun.mz.ui.discussion.DiscussionFragment.Companion.POST_KEY
import com.nahyun.mz.ui.discussion.adapter.DiscussionCommentAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscussionDetailActivity : BaseActivity<ActivityDiscussionDetailBinding>(R.layout.activity_discussion_detail) {

    private lateinit var commentAdapter: DiscussionCommentAdapter
    private val viewModel: DiscussionDetailViewModel by viewModels() {
        DiscussionDetailViewModelFactory((application as MZApplication).repository)
    }

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
                Toast.makeText(this@DiscussionDetailActivity, "내가 쓴 글은 공감할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!post.isLike) {
                Toast.makeText(this@DiscussionDetailActivity, "이 글을 공감하였습니다.", Toast.LENGTH_SHORT).show()
                viewModel.togglePostLike()
            } else {
                Toast.makeText(this@DiscussionDetailActivity, "이미 공감한 글입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 텍스트 선택
        setTranslateActionMode(binding.postTitleTv)
        setTranslateActionMode(binding.postContentTv)
    }

    private fun setTranslateActionMode(textView: TextView) {
        textView.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                // 기본 메뉴 제거
                menu.clear()
                // 커스텀 메뉴 추가
                menu.add(0, 1, 0, "번역하기")
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                if (item.itemId == 1) {
                    val selectedText = getSelectedText(textView)
                    if (selectedText.isNullOrBlank()) return false

                    lifecycleScope.launch {
                        val word = withContext(Dispatchers.IO) {
                            viewModel.getWord(selectedText)
                        }

                        if (word != null) {
                            showWordTooltip(word, textView)
                        } else {
                            Toast.makeText(this@DiscussionDetailActivity, "등록된 단어가 없습니다", Toast.LENGTH_SHORT).show()
                        }
                    }

                    mode.finish()
                    return true
                }
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }
    }

    fun getSelectedText(textView: TextView): String? {
        val start = textView.selectionStart
        val end = textView.selectionEnd
        return if (start >= 0 && end >= 0 && start != end) {
            textView.text.subSequence(start, end).toString()
        } else null
    }

    fun showWordTooltip(word: Word, anchorView: View) {
        val tpBinding = DataBindingUtil.inflate<ItemTooltipBinding>(
            LayoutInflater.from(this),
            R.layout.item_tooltip,
            null,
            false
        )
        tpBinding.word = word

        val popupWindow = PopupWindow(
            tpBinding.root,  // 바인딩의 root view를 전달
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(anchorView)
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