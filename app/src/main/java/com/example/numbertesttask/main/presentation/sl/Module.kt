package com.example.numbertesttask.main.presentation.sl

import androidx.lifecycle.ViewModel

interface Module <T : ViewModel> {
    fun viewModel(): T
}