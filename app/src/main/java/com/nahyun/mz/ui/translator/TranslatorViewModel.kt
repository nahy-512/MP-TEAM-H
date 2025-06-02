package com.nahyun.mz.ui.translator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nahyun.mz.domain.model.Word
import com.nahyun.mz.domain.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TranslatorViewModel(private val repository: WordRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<Word?>()
    val searchResult: LiveData<Word?> = _searchResult

    val favoriteWords: LiveData<List<Word>> = repository.favoriteWords.asLiveData()

    fun searchWord(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.searchWord(query)
            _searchResult.postValue(result)
        }
    }

    fun toggleSearchWordIsLike() {
        val currentIsLike = _searchResult.value!!.isLike
        viewModelScope.launch(Dispatchers.IO) {
            val updatedRows = repository.updateIsLike(_searchResult.value!!.id, !currentIsLike)
            if (updatedRows > 0) {
                updateSearchWordLike(!currentIsLike)
            }
        }
    }

    fun removeFromFavorites(wordId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateIsLike(wordId, false)
            updateSearchWordLike(false)
        }
    }

    private fun updateSearchWordLike(isLike: Boolean) {
        if (_searchResult.value == null) return
        _searchResult.postValue(_searchResult.value!!.copy(
            isLike = isLike
        ))
    }

    fun removeAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeAllFavorites()
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