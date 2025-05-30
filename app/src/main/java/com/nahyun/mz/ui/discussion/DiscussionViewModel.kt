package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.domain.model.Discussion
import com.nahyun.mz.utils.TimeConverter

class DiscussionViewModel : ViewModel() {
    private val _postList = MutableLiveData<List<Discussion>>(listOf())
    val postList: LiveData<List<Discussion>> = _postList

    init {
        getPostList()
    }

    private fun getPostList() {
        val db = Firebase.firestore
        val tempPostList = mutableListOf<Discussion>()

        db.collection(POST_DB)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    tempPostList.add(
                        Discussion(
                            id = document.id.toString().toLong(),
                            title = data["title"].toString(),
                            content = data["content"].toString(),
                            imageUrl = data["imageUrl"] as String?,
                            likeCount = data["likeCount"].toString().toInt(),
                            commentCount = data["commentCount"].toString().toInt(),
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