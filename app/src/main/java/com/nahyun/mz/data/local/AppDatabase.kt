package com.nahyun.mz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nahyun.mz.data.local.dao.WordDao
import com.nahyun.mz.domain.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Word::class],
    version = RoomConstant.ROOM_VERSION
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                var wordDao = database.wordDao()
                scope.launch {
                    wordDao.insertAll(
                        Word.initWordList
                    )
                }
            }
        }
    }

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    RoomConstant.ROOM_DB_NAME
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}