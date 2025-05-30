package com.nahyun.mz.ui.discussion

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
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

    fun fetchData() {
        viewModelScope.launch {
            getUsers()
            getComments(post.id)
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

    fun getComments(postId: Long) {
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
                                nickname = user.nickname,
                                profileImageUrl = user.profileUrl,
                                likeCount = block["likeCount"].toString().toInt(),
                            )
                        )
                    }
                    _commentList.value = tempCommentList
                }
            }
    }

    fun getAuthorProfile() = post.getAuthor(_userList.value!!) ?: User()

    companion object {
        private const val TAG = "DiscussionDetailVM"
        const val USER_DB = "user"
        const val COMMENT_DB = "comment"
    }
}