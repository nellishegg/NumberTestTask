package com.example.numbertesttask.main

import android.app.Application
import com.example.numbertesttask.BuildConfig
import com.example.numbertesttask.numbers.data.cloud.CloudModule

open class NumbersApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val cloudModule = if (BuildConfig.DEBUG)
            CloudModule.Debug()
        else
            CloudModule.Release()
    }
}