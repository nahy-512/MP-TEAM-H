package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahyun.mz.domain.repository.DiscussionRepository
import kotlinx.coroutines.launch

class DiscussionAddViewModel(
    private val repository: DiscussionRepository = DiscussionRepository()
) : ViewModel() {

    var postIdToCreate: Long = -1

    val titleText = MutableLiveData<String>()
    val contentText = MutableLiveData<String>()

    private val _postSuccess = MutableLiveData<Boolean>()
    val postSuccess: LiveData<Boolean> = _postSuccess

    fun initPostId(postSize: Int) {
        Log.d(TAG, "initPostId - postSize: $postSize")
        if (postSize < 0) return
        this.postIdToCreate = (postSize + 1).toLong()
    }

    fun registerPost() {
        val title = titleText.value.orEmpty()
        val content = contentText.value.orEmpty()

        if (postIdToCreate < 0 || title.isBlank() || content.isBlank()) {
            _postSuccess.value = false
            return
        }

        viewModelScope.launch {
            val result = repository.addPost(postIdToCreate, title, content)
            _postSuccess.value = result.isSuccess
        }
    }


    companion object {
        private const val TAG = "DiscussionAddVM"
    }
}