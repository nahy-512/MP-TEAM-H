package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.domain.model.Comment
import com.nahyun.mz.domain.model.Discussion
import com.nahyun.mz.domain.model.User
import kotlinx.coroutines.launch

class DiscussionDetailViewModel : ViewModel() {
    val db = Firebase.firestore

    lateinit var post: Discussion // 게시글 정보

    private val _userList = MutableLiveData<List<User>>(listOf())
    val userList: LiveData<List<User>> = _userList

    val commentList = listOf<Comment>(
        Comment(likeCount = 0),
        Comment(),
        Comment(),
    )

    fun getUsers() {
        val tempUserList = mutableListOf<User>()

        viewModelScope.launch {
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
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    fun getAuthorProfile() = post.getAuthor(_userList.value!!) ?: User()

    companion object {
        private const val TAG = "DiscussionDetailVM"
        const val USER_DB = "user"
    }
}