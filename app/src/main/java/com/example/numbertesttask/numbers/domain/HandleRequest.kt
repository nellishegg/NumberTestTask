package com.example.numbertesttask.numbers.domain

interface HandleRequest {
   suspend fun handle(block: suspend () -> Unit): NumbersResult

    class Base(
        private val handleError: HandleError,
        private val repository: NumbersRepository
    ) : HandleRequest {
        override suspend fun handle(block: suspend () -> Unit) = try {
            block.invoke()
                repository.randomNumberFact()
            NumbersResult.Success(repository.allNumbers())
            } catch (e: Exception) {
            NumbersResult.Failure(handleError.handle(e))
            }

    }
}