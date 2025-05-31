package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.domain.model.Comment
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.domain.model.User
import com.nahyun.mz.ui.discussion.DiscussionViewModel.Companion.POST_DB
import com.nahyun.mz.utils.TimeConverter
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DiscussionDetailViewModel : ViewModel() {
    val db = Firebase.firestore

    var postId: Long = 0L

    // 게시글 정보
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val _userList = MutableLiveData<List<User>>(listOf())
    val userList: LiveData<List<User>> = _userList

    private val _commentList = MutableLiveData<List<Comment>>(listOf())
    val commentList: LiveData<List<Comment>> = _commentList

    val commentText = MutableLiveData<String>()

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean> = _postSuccess

    fun initPost(post: Post) {
        _post.value = post
        postId = post.id
    }

    fun fetchData() {
        viewModelScope.launch {
            getUsers()
            getComments(postId)
        }
    }

    // 댓글 작성
    fun postComment() {
        if (commentText.value.isNullOrBlank()) return
        viewModelScope.launch {
            addComment(postId, USER_ID, commentText.value ?: "")
        }
    }

    // 유저 목록 조회
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

    // 댓글 목록 조회
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
                    updateCommentCount(tempCommentList.size)
                }
            }
    }

    // 게시글 좋아요 여부 변경
    fun togglePostLike() {
        val docRef = db.collection(POST_DB).document(postId.toString())

        db.runTransaction { transaction ->
            // 현재 문서 스냅샷 읽기
            val snapshot = transaction.get(docRef)

            // 기존 isLike, likeCount 가져오기
            val currentIsLike = snapshot.getBoolean("isLike") == true
            val currentCount = (snapshot.getLong("likeCount") ?: 0L).toInt()

            // 새 값 계산
            val newIsLike = !currentIsLike
            val newCount = if (newIsLike) currentCount + 1 else currentCount - 1

            // 업데이트
            transaction.update(
                docRef, mapOf(
                    "isLike" to newIsLike,
                    "likeCount" to newCount
                )
            )
            updateLike(newIsLike, newCount)
            // 트랜잭션 블록은 아무 값(null 등)을 반환
            null
        }
            .addOnSuccessListener {
                Log.d(TAG, "포스트 좋아요 토글 성공")
                // 뷰모델의 suspend getPostList()를 다시 호출하거나,
                // 아래처럼 특정 포스트만 업데이트할 수도 있습니다.
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "포스트 좋아요 토글 실패", e)
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

    // 댓글 좋아요 여부 변경
    fun toggleCommentLike(position: Int) {
        val docRef = db.collection(COMMENT_DB).document(postId.toString())

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
                getComments(postId)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "댓글 좋아요 토글 실패", e)
            }
    }

    fun getAuthorProfile() = post.value!!.getAuthor(_userList.value!!) ?: User()

    private fun updateLike(isLike: Boolean, likeCount: Int) {
        _post.postValue(_post.value!!.copy(
            isLike = isLike,
            likeCount = likeCount
        ))
    }

    private fun updateCommentCount(commentCount: Int) {
        _post.postValue(_post.value!!.copy(
            commentCount = commentCount
        ))
    }

    companion object {
        private const val TAG = "DiscussionDetailVM"
        const val USER_DB = "user"
        const val COMMENT_DB = "comment"
        const val USER_ID = 1
    }
}