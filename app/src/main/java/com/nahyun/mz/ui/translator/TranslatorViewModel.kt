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

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

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
            loadFavorites()
            _isFavorite.postValue(true)
        }
    }

    fun removeFromFavorites(wordId: Int) {
        viewModelScope.launch {
            repository.updateIsLike(wordId, false)
            loadFavorites()
            _isFavorite.postValue(false)
        }
    }

    fun checkIsFavorite() {
        _isFavorite.postValue(_searchResult.value!!.isLike == true)
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