package com.nahyun.mz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nahyun.mz.data.local.RoomConstant
import com.nahyun.mz.domain.model.Word

@Dao
interface WordDao {
    @Insert
    fun insert(word: Word)

    @Insert
    fun insertAll(words: List<Word>)

    @Query("SELECT * FROM ${RoomConstant.Table.WORD}")
    fun getAllWords(): List<Word>

    @Query("SELECT * FROM ${RoomConstant.Table.WORD} WHERE word = :searchWord")
    fun getWordBySearch(searchWord: String): Word
}