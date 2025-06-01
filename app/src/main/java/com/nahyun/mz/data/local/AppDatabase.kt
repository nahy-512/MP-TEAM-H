package com.nahyun.mz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nahyun.mz.data.local.dao.WordDao
import com.nahyun.mz.domain.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [Word::class],
    version = RoomConstant.ROOM_VERSION
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    RoomConstant.ROOM_DB_NAME
                )
                    .addCallback(object : Callback() {

                        val initFoodList = Word.initWordList

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            GlobalScope.launch(Dispatchers.IO) {
                                getInstance(context).run {
                                    // 음식 추가
                                    wordDao().insertAll(
                                        initFoodList
                                    )
                                }
                            }
                        }
                    })
                    .build().also { INSTANCE = it }
            }
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}