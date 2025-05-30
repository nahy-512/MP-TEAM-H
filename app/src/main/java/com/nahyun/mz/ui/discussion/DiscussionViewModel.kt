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
import com.nahyun.mz.utils.TimeConverter
import kotlinx.coroutines.launch

class DiscussionViewModel : ViewModel() {
    private val _postList = MutableLiveData<List<Post>>(listOf())
    val postList: LiveData<List<Post>> = _postList

    init {
        viewModelScope.launch {
            getPostList()
        }
    }

    private fun getPostList() {
        val db = Firebase.firestore
        val tempPostList = mutableListOf<Post>()

        db.collection(POST_DB)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    tempPostList.add(
                        Post(
                            id = document.id.toString().toLong(),
                            title = data["title"].toString(),
                            content = data["content"].toString(),
                            imageUrl = data["imageUrl"] as String?,
                            likeCount = data["likeCount"].toString().toInt(),
                            commentCount = data["commentCount"].toString().toInt(),
                            authorId = data["userId"].toString().toInt(),
                            author = data["author"].toString(),
                            createdAt = TimeConverter.parseTimeStampToLocalDateTime(data["createdAt"] as Timestamp),
                        )
                    )
                }
                tempPostList.sortByDescending { it.createdAt }
                _postList.value = tempPostList
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    companion object {
        private const val TAG = "DiscussionVM"
        const val POST_DB = "post"
    }
}