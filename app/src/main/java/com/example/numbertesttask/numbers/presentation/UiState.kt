package com.example.numbertesttask.numbers.presentation

sealed class UiState {

    class Success() : UiState() {
        override fun equals(other: Any?): Boolean {
            return if (other is Success)true else super.equals(other)
        }
    }

    data class Error(private val message: String) : UiState() {
    }
}
