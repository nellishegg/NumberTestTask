package com.example.numbertesttask.main.presentation.sl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val dependencyContainer: DependencyContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = //порождаем вьюмодельку
        dependencyContainer.module(modelClass).viewModel() as T
}