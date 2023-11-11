package com.example.numbertesttask.main.presentation.sl

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.numbertesttask.BuildConfig
import com.example.numbertesttask.numbers.data.cloud.CloudModule

open class NumbersApp : Application(), ProvideViewModel {

    private lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate() {
        super.onCreate()
        viewModelFactory =
            ViewModelFactory(DependencyContainer.Base(Core.Base(!BuildConfig.DEBUG, this)))

        val cloudModule = if (BuildConfig.DEBUG)
            CloudModule.Debug()
        else
            CloudModule.Release()
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T =
        ViewModelProvider(owner, viewModelFactory)[clazz]

}