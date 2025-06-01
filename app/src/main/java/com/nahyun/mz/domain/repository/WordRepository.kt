package com.nahyun.mz.domain.repository

import com.nahyun.mz.data.local.dao.WordDao
import com.nahyun.mz.domain.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordRepository(
    private val wordDao: WordDao
) {
    // 메모리에 즐겨찾기 저장
    private val favoriteWords = mutableListOf<Word>()

    suspend fun searchWord(query: String): Word = withContext(Dispatchers.IO) {
        wordDao.getWordBySearch(query)
    }

    suspend fun addToFavorites(word: Word) = withContext(Dispatchers.IO) {
        if (!favoriteWords.any { it.word == word.word }) {
            favoriteWords.add(word)
        }
    }

    suspend fun removeFromFavorites(word: String) = withContext(Dispatchers.IO) {
        favoriteWords.removeAll { it.word == word }
    }

    suspend fun isWordFavorite(word: String): Boolean = withContext(Dispatchers.IO) {
        favoriteWords.any { it.word == word }
    }

    suspend fun getFavorites(): List<Word> = withContext(Dispatchers.IO) {
        favoriteWords.toList()
    }
}