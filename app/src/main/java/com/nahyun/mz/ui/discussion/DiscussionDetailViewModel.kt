package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.nahyun.mz.domain.model.Comment
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.domain.model.User
import com.nahyun.mz.domain.repository.DiscussionRepository
import com.nahyun.mz.utils.TimeConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    private val repository: DiscussionRepository = DiscussionRepository()
) : ViewModel() {

    var postId: Long = 0L

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
            val users = repository.fetchUsers()
            _userList.value = users
            loadComments()
        }
    }

    fun postComment() {
        if (commentText.value.isNullOrBlank()) return
        repository.addComment(postId, USER_ID, commentText.value!!,
            onSuccess = {
                commentText.postValue("")
                _postSuccess.postValue(true)
                loadComments()
            },
            onFailure = { Log.e(TAG, "댓글 추가 실패", it) }
        )
    }

    private fun loadComments() {
        viewModelScope.launch {
            try {
                val rawComments = repository.fetchPostComments(postId)
                val userMap = _userList.value?.associateBy { it.id } ?: emptyMap()

                val comments = rawComments.map { block ->
                    val userId = (block["userId"] as DocumentReference).id.toInt()
                    val user = userMap[userId] ?: User()
                    Comment(
                        content = block["body"].toString(),
                        createdAt = TimeConverter.parseTimeStampToLocalDateTime(block["createdAt"] as Timestamp),
                        userId = userId,
                        nickname = user.nickname,
                        profileImageUrl = user.profileUrl,
                        likeCount = (block["likeCount"] as? Number)?.toInt() ?: 0,
                        isLike = block["isLike"] as? Boolean == true
                    )
                }

                _commentList.postValue(comments)
                updateCommentCount(comments.size)
            } catch (e: Exception) {
                Log.e(TAG, "댓글 불러오기 실패", e)
            }
        }
    }

    fun togglePostLike() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.togglePostLike(postId,
                onSuccess = { isLike, likeCount ->
                    updatePostLike(isLike, likeCount)
                },
                onFailure = { Log.e(TAG, "포스트 좋아요 실패", it) }
            )
        }
    }

    fun toggleCommentLike(position: Int) {
        repository.toggleCommentLike(postId, position,
            onSuccess = { loadComments() },
            onFailure = { Log.e(TAG, "댓글 좋아요 실패", it) }
        )
    }

    fun getAuthorProfile() = post.value!!.getAuthor(_userList.value!!) ?: User()

    private fun updatePostLike(isLike: Boolean, likeCount: Int) {
        _post.postValue(_post.value!!.copy(isLike = isLike, likeCount = likeCount))
    }

    private fun updateCommentCount(commentCount: Int) {
        _post.postValue(_post.value!!.copy(commentCount = commentCount))
    }

    companion object {
        private const val TAG = "DiscussionDetailVM"
        const val USER_ID = 1
    }
}