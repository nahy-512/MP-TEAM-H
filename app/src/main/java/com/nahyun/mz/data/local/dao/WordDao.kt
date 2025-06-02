package com.nahyun.mz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nahyun.mz.data.local.RoomConstant
import com.nahyun.mz.domain.model.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert
    suspend fun insert(word: Word)

    @Insert
    suspend fun insertAll(words: List<Word>)

    @Query("SELECT * FROM ${RoomConstant.Table.WORD}")
    fun getAllWords(): List<Word>

    @Query("SELECT * FROM ${RoomConstant.Table.WORD} WHERE word = :searchWord")
    fun getWordBySearch(searchWord: String): Word

    @Query("UPDATE ${RoomConstant.Table.WORD} SET isLike = :isLike, updatedAt = :likedDate WHERE id = :wordId")
    suspend fun updateIsLikeById(wordId: Int, isLike: Boolean, likedDate: Long): Int

    @Query("SELECT * FROM ${RoomConstant.Table.WORD} WHERE isLike = 1 ORDER BY updatedAt DESC")
    fun getLikedWords(): Flow<List<Word>>

    @Query("UPDATE ${RoomConstant.Table.WORD} SET isLike = 0 WHERE isLike = 1")
    suspend fun removeAllFavorites()
}