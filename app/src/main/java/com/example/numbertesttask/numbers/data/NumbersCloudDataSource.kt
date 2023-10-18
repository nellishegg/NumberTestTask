package com.example.numbertesttask.numbers.data

import java.lang.Exception
import java.net.UnknownHostException

interface NumbersCloudDataSource : FetchNumber {
    suspend fun randomNumber(): NumbersData

    class Base(private val service: NumbersService) : NumbersCloudDataSource {

        override suspend fun randomNumber(): NumbersData {
            val response = service.random()
            val body = response.body() ?: throw IllegalStateException("service unavailable")
            val headers = response.headers()
            headers.find { (key, _) ->
                key == RANDOM_API_HEADERS
            }?.let { (_, value) ->
                return NumbersData(value, body)
            }
            throw IllegalStateException("service unavailable")

        }

        override suspend fun number(number: String): NumbersData {
            val fact = service.fact(number)
            return NumbersData(number, fact)
        }

        companion object {
            private const val RANDOM_API_HEADERS = "X-Numbers-API-Number"
        }
    }
}