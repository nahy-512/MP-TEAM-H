package com.nahyun.mz

import android.app.Application
// import com.nahyun.mz.data.local.WordDatabase

class MZApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // WordDatabase.initialize(this)
    }
}