package com.nahyun.mz.ui.discussion

import androidx.lifecycle.ViewModel
import com.nahyun.mz.domain.model.Discussion

class DiscussionViewModel : ViewModel() {
    val postList = listOf<Discussion>(
        Discussion(
            0,
            "밈 세상에서 살아남기",
            "내용입니다",
            "",
            10, 100,
            "코코아",
            "10분 전"
        ),
        Discussion(
            likeCount = 0,
            commentCount = 0,
        ),
        Discussion(
            likeCount = 12,
            commentCount = 0,
        ),
        Discussion(
            likeCount = 0,
            commentCount = 8,
        ),
        Discussion(),
        Discussion()
    )
}