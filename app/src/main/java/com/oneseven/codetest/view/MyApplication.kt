package com.oneseven.codetest.view

import android.app.Application
import com.oneseven.codetest.viewmodel.myModule
import com.oneseven.codetest.viewmodel.repoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@MyApplication)
            // modules
            val list = listOf(myModule, repoModule)
            modules(list)
        }
    }
}