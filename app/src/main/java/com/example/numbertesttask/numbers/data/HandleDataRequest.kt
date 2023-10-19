package com.example.numbertesttask.numbers.data

import com.example.numbertesttask.numbers.data.cache.NumbersCacheDataSource
import com.example.numbertesttask.numbers.domain.HandleError
import com.example.numbertesttask.numbers.domain.NumberFact

interface HandleDataRequest {
    suspend fun handle(block: suspend () -> NumbersData): NumberFact
    class Base(
        private val cacheDataSource: NumbersCacheDataSource,
        private val mapperToDomain: NumbersData.Mapper<NumberFact>,
        private val handleError: HandleError<Exception>

    ) : HandleDataRequest {
        override suspend fun handle(block: suspend () -> NumbersData) = try {
            val result = block.invoke()
            cacheDataSource.saveNumber(result)
            result.map(mapperToDomain)
        } catch (e: Exception) {
            throw handleError.handle(e)
        }
    }
}