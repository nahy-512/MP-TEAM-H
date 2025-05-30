package com.nahyun.mz.domain.model

import java.io.Serializable
import java.time.LocalDateTime

data class Post(
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
    val content: String,
    val createdAt: LocalDateTime,
    val userId: Int = 0,
    val nickname: String,
    val profileImageUrl: String?,
    val isLike: Boolean = false,
    val likeCount: Int = 3
)