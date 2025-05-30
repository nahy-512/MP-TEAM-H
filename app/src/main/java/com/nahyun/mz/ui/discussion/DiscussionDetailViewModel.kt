package com.nahyun.mz.ui.discussion

import androidx.lifecycle.ViewModel
import com.nahyun.mz.domain.model.Comment
import com.nahyun.mz.domain.model.Discussion

class DiscussionDetailViewModel : ViewModel() {
    lateinit var post: Discussion

    val commentList = listOf<Comment>(
        Comment(likeCount = 0),
        Comment(),
        Comment(),
    )
}