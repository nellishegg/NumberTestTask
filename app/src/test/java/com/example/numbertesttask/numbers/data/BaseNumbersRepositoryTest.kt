package com.example.numbertesttask.numbers.data

import com.example.numbertesttask.numbers.domain.NumbersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseNumbersRepositoryTest {

    private lateinit var repository: NumbersRepository
    private lateinit var cloudDataSource: TestNumbersCloudDataSource
    private lateinit var cacheDataSource: TestNumbersCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = TestNumbersCloudDataSource()
        cacheDataSource = TestNumbersCacheDataSource()
        repository = BaseNumbersRepository(cloudDataSource, cacheDataSource)
    }

    @Test
    fun test_all_numbers() = runBlocking {
        cacheDataSource.replaceData(
            listOf(
                NumbersData("4", "fact of 4"),
                NumbersData("5", "fact of 5")
            )
        )
        val actual = repository.allNumbers()
        val expected = listOf(
            NumbersData("4", "fact of 4"),
            NumbersData("5", "fact of 5")
        )
        actual.forEachIndexed { index, item ->
            assertEquals(expected[index], item)
        }
        assertEquals(1, cacheDataSource.allNumbersCallCount)
    }

    @Test
    fun test_number_fact_not_cached_success() = runBlocking {
        cloudDataSource.makeExpected(NumbersData("10", "fact about 10"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.numberFact("10")
        val expected = NumbersData("10", " fact about 10")

        assertEquals(expected, actual)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(expected, cacheDataSource.data[0])
    }

    @Test
    fun test_number_fact_not_cached_failure() {

    }

    @Test
    fun test_number_fact_cached() {

    }

    @Test
    fun test_random_number_fact_not_cached_success() {
    }

    fun test_random_number_fact_not_cached_failure() {}
    fun test_random_number_fact_cached() {}

    private class TestNumbersCloudDataSource : NumbersCloudDataSource {

        private var thereIsConnection = true
        private var numbersData = NumbersData("", "")

        fun changeConnection(connected: Boolean) {
            thereIsConnection = connected
        }

        fun makeExpected(numbers: NumbersData) {
            numbersData = numbers
        }

        override suspend fun numberFact(): NumbersData {
            return if (thereIsConnection)
                numbersData
            else
                throw UnknownHostException()
        }
    }

    private class TestNumbersCacheDataSource : NumbersCacheDataSource {
        var numberFactCalled = mutableListOf<String>()
        var allNumbersCallCount = 0
        var saveNumberFactCalledCount = 0
        val data = mutableListOf<NumbersData>()

        fun replaceData(newData: List<NumbersData>) {
            data.clear()
            data.addAll(newData)
        }

        override suspend fun allNumbers(): List<NumbersData> {
            allNumbersCallCount++
            return data
        }

        override suspend fun numberFact(number: String): NumbersData {
            numberFactCalled.add(number)
            return data[0]
        }

        override suspend fun saveNumberFact(numberData: NumbersData) {
            saveNumberFactCalledCount++
            data.add(numberData)
        }
    }
}