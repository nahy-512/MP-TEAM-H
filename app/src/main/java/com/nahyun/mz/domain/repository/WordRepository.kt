package com.nahyun.mz.domain.repository

import com.nahyun.mz.data.local.dao.WordDao
import com.nahyun.mz.domain.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordRepository(
    private val wordDao: WordDao
) {
    suspend fun getAllWord(): List<Word> = withContext(Dispatchers.IO) {
        wordDao.getAllWords()
    }

    suspend fun searchWord(query: String): Word = withContext(Dispatchers.IO) {
        wordDao.getWordBySearch(query)
    }

    suspend fun updateIsLike(wordId: Int, isLike: Boolean) = withContext(Dispatchers.IO) {
        wordDao.updateIsLikeById(wordId, isLike)
    }

    suspend fun removeFromFavorites(word: String) = withContext(Dispatchers.IO) {
//        favoriteWords.removeAll { it.word == word }
    }

    suspend fun getFavorites(): List<Word> = withContext(Dispatchers.IO) {
        wordDao.getLikedWords()
    }
}