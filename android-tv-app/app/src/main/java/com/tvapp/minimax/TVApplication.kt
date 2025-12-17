package com.tvapp.minimax

import android.app.Application
import android.content.Context

class TVApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TVApplication
            private set

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}