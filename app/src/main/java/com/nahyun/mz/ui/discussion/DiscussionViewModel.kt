package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.ui.discussion.DiscussionDetailViewModel.Companion.COMMENT_DB
import com.nahyun.mz.utils.TimeConverter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DiscussionViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _postList = MutableLiveData<List<Post>>(listOf())
    val postList: LiveData<List<Post>> = _postList

    init {
        viewModelScope.launch {
            getPostList()
        }
    }

    suspend fun getPostList() {
        val tempPostList = mutableListOf<Post>()

        try {
            val result = db.collection(POST_DB).get().await()

            for (document in result) {
                val data = document.data
                val postId = document.id.toString().toLong()
                val commentCount = getCommentCount(postId)

                tempPostList.add(
                    Post(
                        id = postId,
                        title = data["title"].toString(),
                        content = data["content"].toString(),
                        imageUrl = data["imageUrl"] as String?,
                        likeCount = data["likeCount"].toString().toInt(),
                        commentCount = commentCount,
                        authorId = data["userId"].toString().toInt(),
                        author = data["author"].toString(),
                        createdAt = TimeConverter.parseTimeStampToLocalDateTime(data["createdAt"] as Timestamp),
                    )
                )
            }

            tempPostList.sortByDescending { it.createdAt }
            _postList.value = tempPostList

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching posts", e)
        }
    }

    suspend fun getCommentCount(postId: Long): Int = suspendCoroutine { continuation ->
        db.collection(COMMENT_DB).document(postId.toString())
            .get()
            .addOnSuccessListener { document ->
                val comments = document.get("comments") as? List<Map<String, Any>> ?: emptyList()
                continuation.resume(comments.size)
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }


    companion object {
        private const val TAG = "DiscussionVM"
        const val POST_DB = "post"
    }
}