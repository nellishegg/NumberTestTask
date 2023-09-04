package com.example.numbertesttask.numbers.presentation

import androidx.lifecycle.viewModelScope
import com.example.numbertesttask.numbers.domain.NumbersResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface HandleNumbersRequest {
    fun handle(
        coroutineScope: CoroutineScope,
        block: suspend () -> NumbersResult
    )

    class Base(
        private val dispatchers: DispatchersList,
        private val communications: NumbersCommunication,
        private val numberResultMapper: NumbersResult.Mapper<Unit>
    ) : HandleNumbersRequest {

        override fun handle(
            coroutineScope: CoroutineScope,
            block: suspend () -> NumbersResult
        ) {
            communications.showProgress(true)
            coroutineScope.launch(dispatchers.io()) {
                val result = block.invoke()
                communications.showProgress(false)
                result.map(numberResultMapper)
            }
        }
    }
}