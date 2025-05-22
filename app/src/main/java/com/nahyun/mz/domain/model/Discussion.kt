package com.nahyun.mz.domain.model

data class Discussion(
    val id: Long = 0L,
    val title: String = "제목",
    val content: String = "내용",
    val image: String = "",
    val likeCount: Int = 10,
    val commentCount: Int = 16,
    val author: String = "닉네임",
    val createAt: String = "25.05.12"
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
