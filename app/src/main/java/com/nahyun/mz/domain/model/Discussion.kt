package com.nahyun.mz.domain.model

import java.io.Serializable
import java.time.LocalDateTime

data class Discussion(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val likeCount: Int,
    val commentCount: Int,
    val authorId: Int,
    val author: String,
    val createdAt: LocalDateTime
): Serializable {
    fun getAuthor(userList: List<User>): User? {
        return userList.find {
            it.id.toLong() == this.id
        }
    }
}

data class Comment(
    val commentId: Int = 0,
    val content: String = "댓글 내용",
    val createAt: String = "5/12 18:30",
    val nickname: String = "닉네임",
    val profileImageUrl: String = "",
    val likeCount: Int = 3
)