package com.nahyun.mz.ui.discussion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahyun.mz.domain.model.Post
import com.nahyun.mz.domain.repository.DiscussionRepository
import kotlinx.coroutines.launch

class DiscussionViewModel(
    private val repository: DiscussionRepository = DiscussionRepository()
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _postList = MutableLiveData<List<Post>>(listOf())
    val postList: LiveData<List<Post>> = _postList

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val posts = repository.fetchAllPosts()
                _postList.value = posts
            } catch (e: Exception) {
                Log.e(TAG, "Error loading posts", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "DiscussionVM"
    }
}