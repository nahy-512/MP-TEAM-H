package com.nahyun.mz.ui.translator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nahyun.mz.domain.model.Word
import com.nahyun.mz.domain.repository.WordRepository
import kotlinx.coroutines.launch

class TranslatorViewModel(private val repository: WordRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<Word?>()
    val searchResult: LiveData<Word?> = _searchResult

    private val _favorites = MutableLiveData<List<Word>>()
    val favorites: LiveData<List<Word>> = _favorites

    init {
        loadFavorites()
    }

    fun searchWord(query: String) {
        viewModelScope.launch {
            val result = repository.searchWord(query)
            _searchResult.postValue(result)
        }
    }

    fun addToFavorites(wordId: Int) {
        viewModelScope.launch {
            repository.updateIsLike(wordId, true)
            updateWordLike(true)
            loadFavorites()
        }
    }

    fun removeFromFavorites(wordId: Int) {
        viewModelScope.launch {
            repository.updateIsLike(wordId, false)
            updateWordLike(false)
            loadFavorites()
        }
    }

    private fun updateWordLike(isLike: Boolean) {
        _searchResult.value = _searchResult.value!!.copy(
            isLike = isLike
        )
    }

    fun removeAllFavorites() {
        viewModelScope.launch {
            repository.removeAllFavorites()
            loadFavorites()
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val favList = repository.getFavorites()
            _favorites.postValue(favList)
        }
    }
}

class TranslatorViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TranslatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TranslatorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}