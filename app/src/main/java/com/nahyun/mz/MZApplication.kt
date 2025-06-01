package com.nahyun.mz

import android.app.Application
import com.nahyun.mz.data.local.AppDatabase
import com.nahyun.mz.domain.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MZApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getInstance(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }

    override fun onCreate() {
        super.onCreate()
        // WordDatabase.initialize(this)
    }
}