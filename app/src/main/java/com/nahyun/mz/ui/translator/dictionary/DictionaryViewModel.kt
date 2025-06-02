package com.nahyun.mz.ui.translator.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nahyun.mz.domain.model.Word
import com.nahyun.mz.domain.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repository: WordRepository) : ViewModel() {

    private val _wordList = MutableLiveData<List<Word>>()
    val wordList: LiveData<List<Word>> = _wordList

    fun getAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            _wordList.postValue(repository.getAllWord())
        }
    }

    fun updateWordLike(wordId: Int, isLike: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateIsLike(wordId, !isLike)
            getAllWords()
        }
    }
}

class DictionaryViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DictionaryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}