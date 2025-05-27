package com.nahyun.mz.data.repository

import com.nahyun.mz.data.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordRepository {

    // 메모리에 즐겨찾기 저장
    private val favoriteWords = mutableListOf<Word>()

    private val predefinedWords = mapOf(
        "갬성" to "감성을 뜻하는 신조어. 감성을 독특하게 표현한 말이다.",
        "군싹" to "군침이 싹 도는 모습을 줄인 신조어.",
        "머선129" to "'무슨 일이야'를 특이하게 표현한 신조어.",
        "별다줄" to "별걸 다 줄인다의 줄임말. 기존 용어를 과도하게 축약하는 현상을 빗댄 신조어.",
        "어쩔티비" to "상대방의 말에 반응할 때 '어쩌라고'라는 의미로 사용되는 신조어.",
        "캠밥" to "캠핑 밥의 줄임말로, 캠핑장에서 먹는 식사를 의미하는 신조어."
    )

    suspend fun searchWord(query: String): Word? = withContext(Dispatchers.IO) {
        predefinedWords[query]?.let { meaning ->
            Word(query, meaning)
        }
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