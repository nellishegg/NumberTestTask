package com.example.numbertesttask.numbers.data.cache

import com.example.numbertesttask.numbers.data.NumbersData
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface NumbersCacheDataSource : FetchNumber {
    suspend fun allNumbers(): List<NumbersData>

    suspend fun contains(number: String): Boolean

    suspend fun saveNumber(numberData: NumbersData)

    class Base(
        private val dao: NumbersDao,
        private val dataToCache: NumbersData.Mapper<NumberCache>
    ) : NumbersCacheDataSource {

        private val mutex = Mutex()
        override suspend fun allNumbers(): List<NumbersData> = mutex.withLock {
            val data = dao.allNumbers()
            return data.map { NumbersData(it.number, it.fact) }
        }

        override suspend fun contains(number: String): Boolean = mutex.withLock {
            val data = dao.number(number)
            return data != null
        }

        override suspend fun saveNumber(numberData: NumbersData) = mutex.withLock {
            dao.insert(numberData.map(dataToCache))
        }

        override suspend fun number(number: String): NumbersData = mutex.withLock {

            val numberCache = dao.number(number) ?: NumberCache("", "", 0)
            return NumbersData(numberCache.number, numberCache.fact)
        }
    }
}

interface FetchNumber {
    suspend fun number(number: String): NumbersData
}