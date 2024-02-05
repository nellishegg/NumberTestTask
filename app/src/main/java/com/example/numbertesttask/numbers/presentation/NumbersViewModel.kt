package com.example.numbertesttask.numbers.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numbertesttask.R
import com.example.numbertesttask.numbers.domain.NumbersInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class NumbersViewModel(
    private val handleResult: HandleNumbersRequest,
    private val manageResources: ManageResources,
    private val communications: NumbersCommunication,
    private val interactor: NumbersInteractor
) : ViewModel() {

    fun observeProgress(owner: LifecycleOwner, observer: Observer<Int>) =
        communications.observeProgress(owner, observer)

    fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) =
        communications.observeState(owner, observer)

    fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) =
        communications.observeList(owner, observer)

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            handleResult.handle(viewModelScope) {
                interactor.init()
            }
        }
    }

    fun fetchRandoNumberFact() = handleResult.handle(viewModelScope) {
        interactor.factAboutRandomNumber()
    }

    fun fetchNumberFact(number: String) {
        if (number.isEmpty()) {
            communications.showState((UiState.ShowError(manageResources.string(R.string.empty_number_error_message))))
        } else
            handleResult.handle(viewModelScope) {
                interactor.factAboutNumber(number)
            }
    }

    fun clearError() {
        communications.showState(UiState.ClearError())
    }
}


interface FetchNumbers {
    fun init(isFirstRun: Boolean)
    fun fetchRandoNumberFact()
    fun fetchNumberFact(number: String)
}

interface ClearError {
    fun clearError()
}

interface DispatchersList {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base : DispatchersList {
        override fun io(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        override fun ui(): CoroutineDispatcher {
            return Dispatchers.Main
        }
    }
}