package com.example.brianmj.nasajson

import android.app.Application
import android.content.Context

class MyApp : Application(){
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MyApp

        val applicationContext: Context
        get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
    }
}