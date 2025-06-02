package com.nahyun.mz.ui.discussion

import android.widget.Toast
import androidx.activity.viewModels
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityDiscussionAddBinding
import com.nahyun.mz.ui.base.BaseActivity
import com.nahyun.mz.ui.discussion.DiscussionFragment.Companion.POST_SIZE_KEY

class DiscussionAddActivity : BaseActivity<ActivityDiscussionAddBinding>(R.layout.activity_discussion_add) {
    private val viewModel: DiscussionAddViewModel by viewModels()

    override fun setup() {
        viewModel.initPostId(intent.getIntExtra(POST_SIZE_KEY, -1))

        binding.apply {
            viewModel = this@DiscussionAddActivity.viewModel
            lifecycleOwner = this@DiscussionAddActivity
        }

        initObserves()
    }

    private fun initObserves() {
        viewModel.postSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "게시글 등록이 완료되었습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}