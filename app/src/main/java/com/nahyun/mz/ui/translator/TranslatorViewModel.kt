package com.nahyun.mz.ui.translator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nahyun.mz.data.Word
import com.nahyun.mz.data.repository.WordRepository
import kotlinx.coroutines.launch

class TranslatorViewModel : ViewModel() {

    private val repository = WordRepository()

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

    fun addToFavorites(word: Word) {
        viewModelScope.launch {
            repository.addToFavorites(word)
            loadFavorites()
            _isFavorite.postValue(true)
        }
    }

    fun removeFromFavorites(word: String) {
        viewModelScope.launch {
            repository.removeFromFavorites(word)
            loadFavorites()
            _isFavorite.postValue(false)
        }
    }

    fun checkIsFavorite(word: String) {
        viewModelScope.launch {
            val isFav = repository.isWordFavorite(word)
            _isFavorite.postValue(isFav)
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val favList = repository.getFavorites()
            _favorites.postValue(favList)
        }
    }
}