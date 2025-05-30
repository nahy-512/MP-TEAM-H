package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.domain.model.Comment
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.domain.model.User
import com.nahyun.mz.utils.TimeConverter
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DiscussionDetailViewModel : ViewModel() {
    val db = Firebase.firestore

    lateinit var post: Post // 게시글 정보

    private val _userList = MutableLiveData<List<User>>(listOf())
    val userList: LiveData<List<User>> = _userList

    private val _commentList = MutableLiveData<List<Comment>>(listOf())
    val commentList: LiveData<List<Comment>> = _commentList

    val commentText = MutableLiveData<String>()

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean> = _postSuccess

    fun fetchData() {
        viewModelScope.launch {
            getUsers()
            getComments(post.id)
        }
    }

    // 댓글 작성
    fun postComment() {
        if (commentText.value.isNullOrBlank()) return
        viewModelScope.launch {
            addComment(post.id, USER_ID, commentText.value ?: "")
        }
    }

    private suspend fun getUsers() = suspendCoroutine { continuation ->
        val tempUserList = mutableListOf<User>()

        db.collection(USER_DB)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    tempUserList.add(
                        User(
                            id = document.id.toString().toInt(),
                            nickname = data["nickname"].toString(),
                            profileUrl = data["profileUrl"] as String?
                        )
                    )
                }
                _userList.value = tempUserList
                continuation.resume(tempUserList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                continuation.resumeWithException(exception)
            }
    }

    private fun getComments(postId: Long) {
        val tempCommentList = mutableListOf<Comment>()

        // 파이어베이스 postId에 해당하는 댓글 목록 가져오기
        db.collection(COMMENT_DB).document(postId.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val comments = document.get("comments") as? List<Map<String, Any>> ?: listOf()

                    for (block in comments) {
                        var userId = (block["userId"] as DocumentReference).id.toInt()
                        var user = _userList.value!!.find { it.id == userId } ?: User()
                        tempCommentList.add(
                            Comment(
                                content = block["body"].toString(),
                                createdAt = TimeConverter.parseTimeStampToLocalDateTime(block["createdAt"] as Timestamp),
                                userId = userId,
                                nickname = user.nickname,
                                profileImageUrl = user.profileUrl,
                                likeCount = block["likeCount"]?.toString()?.toInt() ?: 0,
                                isLike = block["isLike"] as? Boolean == true
                            )
                        )
                    }
                    _commentList.value = tempCommentList
                }
            }
    }

    // 댓글 추가
    private fun addComment(postId: Long, userId: Int, content: String) {
        val commentMap = mapOf(
            "userId" to db.collection(USER_DB).document(userId.toString()),
            "body" to content,
            "createdAt" to Timestamp.now(),
            "likeCount" to 0,
            "isLike" to false
        )

        db.collection(COMMENT_DB).document(postId.toString())
            .update("comments", FieldValue.arrayUnion(commentMap))
            .addOnSuccessListener {
                Log.d(TAG, "댓글 추가 성공")
                getComments(postId) // 댓글 추가 후 갱신
                commentText.value = ""
                _postSuccess.value = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "댓글 추가 실패", e)
            }
    }

    fun toggleCommentLike(position: Int) {
        val docRef = db.collection(COMMENT_DB).document(post.id.toString())

        db.runTransaction { transaction ->
            // 문서 스냅샷 가져오기
            val snapshot = transaction.get(docRef)

            // comments 필드 읽어서 MutableList로 변환
            @Suppress("UNCHECKED_CAST")
            val comments = snapshot.get("comments") as? List<Map<String, Any>> ?: emptyList()
            val mutableComments = comments.map { it.toMutableMap() }.toMutableList()

            // 클릭된 댓글 데이터 꺼내기
            val comment = mutableComments[position]

            // 기존 값 읽기
            val currentIsLike = comment["isLike"] as? Boolean == true
            val currentCount = (comment["likeCount"] as? Number)?.toInt() ?: 0

            // 토글 후 값 설정
            val newIsLike = !currentIsLike
            comment["isLike"] = newIsLike
            comment["likeCount"] = if (newIsLike) currentCount + 1 else currentCount - 1

            // 다시 리스트에 넣고 업데이트
            mutableComments[position] = comment
            transaction.update(docRef, "comments", mutableComments)
            // 트랜잭션 블록은 Unit(빈 값) 반환
            null
        }
            .addOnSuccessListener {
                Log.d(TAG, "댓글 좋아요 토글 성공")
                // 로컬 리스트도 갱신하거나, getComments(postId)를 호출해서 UI 리프레시
                getComments(post.id)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "댓글 좋아요 토글 실패", e)
            }
    }

    fun getAuthorProfile() = post.getAuthor(_userList.value!!) ?: User()

    companion object {
        private const val TAG = "DiscussionDetailVM"
        const val USER_DB = "user"
        const val COMMENT_DB = "comment"
        const val USER_ID = 1
    }
}