package com.example.numbertesttask.main.presentation.sl

import android.content.Context
import com.example.numbertesttask.numbers.data.cache.CacheModule
import com.example.numbertesttask.numbers.data.cache.NumbersDataBase
import com.example.numbertesttask.numbers.data.cloud.CloudModule
import com.example.numbertesttask.numbers.presentation.DispatchersList
import com.example.numbertesttask.numbers.presentation.ManageResources

interface Core : CloudModule, CacheModule, ManageResources {

    fun provideDispatchers(): DispatchersList

    class Base(
        private val isRelease: Boolean,
        context: Context
    ) : Core {

        private val manageResources: ManageResources = ManageResources.Base(context)

        private val dispatchersList by lazy {
            DispatchersList.Base()
        }

        private val cloudModule by lazy {
            if (isRelease)
                CloudModule.Release()
            else
                CloudModule.Debug()
        }
        private val cacheModule by lazy {
            if (isRelease)
                CacheModule.Base(context)
            else
                CacheModule.Mock(context)
        }

        override fun <T> service(classz: Class<T>): T =
            cloudModule.service(classz)

        override fun provideDataBase(): NumbersDataBase = cacheModule.provideDataBase()

        override fun string(id: Int): String = manageResources.string(id)

        override fun provideDispatchers(): DispatchersList = dispatchersList

    }
}