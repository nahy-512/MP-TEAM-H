package com.nahyun.mz.domain.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.domain.model.User
import com.nahyun.mz.utils.TimeConverter
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DiscussionRepository {
    private val db = Firebase.firestore

    // 게시글 목록 조회
    suspend fun fetchAllPosts(): List<Post> {
        val tempPostList = mutableListOf<Post>()
        val result = db.collection(POST_DB).get().await()

        for (document in result) {
            val data = document.data
            val postId = document.id.toLong()
            val commentCount = getCommentCount(postId)

            tempPostList.add(
                Post(
                    id = postId,
                    title = data["title"].toString(),
                    content = data["content"].toString(),
                    imageUrl = data["imageUrl"] as String?,
                    isLike = data["isLike"] as? Boolean == true,
                    likeCount = data["likeCount"].toString().toInt(),
                    commentCount = commentCount,
                    authorId = data["userId"].toString().toInt(),
                    author = data["author"].toString(),
                    createdAt = TimeConverter.parseTimeStampToLocalDateTime(data["createdAt"] as Timestamp),
                )
            )
        }
        return tempPostList.sortedByDescending { it.createdAt }
    }

    // 댓글 수 조회
    private suspend fun getCommentCount(postId: Long): Int = suspendCoroutine { cont ->
        db.collection(COMMENT_DB).document(postId.toString())
            .get()
            .addOnSuccessListener { doc ->
                val comments = doc.get("comments") as? List<Map<String, Any>> ?: emptyList()
                cont.resume(comments.size)
            }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    // 유저 목록 조회
    suspend fun fetchUsers(): List<User> = suspendCoroutine { cont ->
        db.collection(USER_DB).get()
            .addOnSuccessListener { result ->
                val userList = result.map { doc ->
                    val data = doc.data
                    User(
                        id = doc.id.toInt(),
                        nickname = data["nickname"].toString(),
                        profileUrl = data["profileUrl"] as String?
                    )
                }
                cont.resume(userList)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    // 게시글의 댓글 목록 조회
    suspend fun fetchPostComments(postId: Long): List<Map<String, Any>> = suspendCoroutine { cont ->
        db.collection(COMMENT_DB).document(postId.toString())
            .get()
            .addOnSuccessListener { doc ->
                val comments = doc.get("comments") as? List<Map<String, Any>> ?: emptyList()
                cont.resume(comments)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    // 게시글 좋아요 여부 변경
    fun togglePostLike(
        postId: Long,
        onSuccess: (Boolean, Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = db.collection(POST_DB).document(postId.toString())

        db.runTransaction { tx ->
            // 현재 문서 스냅샷 읽기
            val snapshot = tx.get(docRef)
            // 기존 isLike, likeCount 가져오기
            val currentIsLike = snapshot.getBoolean("isLike") == true
            val currentCount = (snapshot.getLong("likeCount") ?: 0L).toInt()
            // 새 값 계산
            val newIsLike = !currentIsLike
            val newCount = if (newIsLike) currentCount + 1 else currentCount - 1
            // 업데이트
            tx.update(
                docRef, mapOf(
                    "isLike" to newIsLike,
                    "likeCount" to newCount
                )
            )
            Pair(newIsLike, newCount) // 값을 트랜잭션 결과로 반환
        }
            .addOnSuccessListener { (newIsLike, newCount) ->
                onSuccess(newIsLike, newCount)
            }
            .addOnFailureListener(onFailure)
    }

    // 댓글 작성
    fun addComment(
        postId: Long,
        userId: Int,
        content: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val commentMap = mapOf(
            "userId" to db.collection(USER_DB).document(userId.toString()),
            "body" to content,
            "createdAt" to Timestamp.now(),
            "likeCount" to 0,
            "isLike" to false
        )

        db.collection(COMMENT_DB).document(postId.toString())
            .update("comments", FieldValue.arrayUnion(commentMap))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    // 댓글 좋아요 여부 변경
    fun toggleCommentLike(
        postId: Long,
        position: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val docRef = db.collection(COMMENT_DB).document(postId.toString())
        db.runTransaction { tx ->
            // 문서 스냅샷 가져오기
            val snapshot = tx.get(docRef)

            // comments 필드 읽어서 MutableList로 변환
            val comments = (snapshot.get("comments") as? List<Map<String, Any>>)?.map { it.toMutableMap() }?.toMutableList() ?: mutableListOf()
            // 클릭된 댓글 데이터 꺼내기
            val comment = comments[position]
            // 기존 값 읽기
            val currentIsLike = comment["isLike"] as? Boolean == true
            val currentCount = (comment["likeCount"] as? Number)?.toInt() ?: 0
            // 토글 후 값 설정
            comment["isLike"] = !currentIsLike
            comment["likeCount"] = if (!currentIsLike) currentCount + 1 else currentCount - 1
            // 다시 리스트에 넣고 업데이트
            comments[position] = comment
            tx.update(docRef, "comments", comments)
            null
        }
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    companion object {
        private const val POST_DB = "post"
        private const val COMMENT_DB = "comment"
        private const val USER_DB = "user"
    }
}