package com.nahyun.mz.domain.model

import java.io.Serializable

data class Discussion(
    val id: Long = 0L,
    val title: String = "제목",
    val content: String = "내용",
    val image: String = "",
    val likeCount: Int = 10,
    val commentCount: Int = 16,
    val author: String = "닉네임",
    val createAt: String = "25.05.12"
): Serializable

data class Comment(
    val commentId: Int = 0,
    val content: String = "댓글 내용",
    val createAt: String = "5/12 18:30",
    val userNickname: String = "닉네임",
    val userProfileImageUrl: String = "",
    val likeCount: Int = 3
)

//data class Discussion(
//    val id: Long,
//    val title: String,
//    val content: String,
//    val image: String,
//    val likeCount: Int,
//    val commentCount: Int,
//    val author: String,
//    val createAt: String
//)
