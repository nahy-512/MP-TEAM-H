package com.nahyun.mz

import android.app.Application
import com.nahyun.mz.data.local.AppDatabase
import com.nahyun.mz.domain.repository.WordRepository

class MZApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { WordRepository(database.wordDao()) }

    override fun onCreate() {
        super.onCreate()
        // WordDatabase.initialize(this)
    }
}