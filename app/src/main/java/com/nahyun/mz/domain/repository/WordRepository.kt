package com.nahyun.mz.domain.repository

import com.nahyun.mz.data.local.dao.WordDao
import com.nahyun.mz.domain.model.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(
    private val wordDao: WordDao
) {
    val favoriteWords: Flow<List<Word>> = wordDao.getLikedWords()

    fun getAllWord(): List<Word> = wordDao.getAllWords()

    fun searchWord(query: String): Word = wordDao.getWordBySearch(query)

    suspend fun updateIsLike(wordId: Int, isLike: Boolean, currentTime: Long = System.currentTimeMillis()) =
        wordDao.updateIsLikeById(wordId, isLike, currentTime)

    suspend fun removeAllFavorites() = wordDao.removeAllFavorites()
}