package com.example.numbertesttask.numbers.data

import com.example.numbertesttask.numbers.domain.NoInternetConnectionException
import com.example.numbertesttask.numbers.domain.NumberFact
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
            NumberFact("4", "fact of 4"),
            NumberFact("5", "fact of 5")
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
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(expected, cacheDataSource.data[0])
    }

    @Test(expected = NoInternetConnectionException::class)
    fun test_number_fact_not_cached_failure() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.numberFact("10")
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_number_fact_cached() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumbersData("10", "cloud 10"))
        cacheDataSource.replaceData(listOf(NumbersData("10", "fact about")))

        val actual = repository.numberFact("10")
        val expected = NumbersData("10", "fact about")

        assertEquals(expected, actual)
        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cacheDataSource.numberFactCalled.size)
        assertEquals(actual, cacheDataSource.numberFactCalled[0])
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_random_number_fact_not_cached_success() = runBlocking {
        cloudDataSource.makeExpected(NumbersData("10", "fact about 10"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.randomNumberFact()
        val expected = NumbersData("10", " fact about 10")

        assertEquals(expected, actual)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(expected, cacheDataSource.data[0])
    }

    @Test(expected = NoInternetConnectionException::class)
    fun test_random_number_fact_not_cached_failure() = runBlocking {
        cloudDataSource.changeConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.randomNumberFact()
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cloudDataSource.numberFactCalledCount)

        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount)

    }

    @Test
    fun test_random_number_fact_cached() = runBlocking {
        cloudDataSource.changeConnection(true)
        cloudDataSource.makeExpected(NumbersData("10", "cloud 10"))
        cacheDataSource.replaceData(listOf(NumbersData("10", "fact about")))

        val actual = repository.randomNumberFact()
        val expected = NumbersData("10", "cloud about")

        assertEquals(expected, actual)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)

        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)

        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

    private class TestNumbersCloudDataSource : NumbersCloudDataSource {

        private var thereIsConnection = true
        private var numbersData = NumbersData("", "")
        var numberFactCalledCount = 0
        var randomNumberFactCalledCount = 0

        fun changeConnection(connected: Boolean) {
            thereIsConnection = connected
        }

        fun makeExpected(numbers: NumbersData) {
            numbersData = numbers
        }

        override suspend fun numberFact(): NumbersData {
            numberFactCalledCount++
            return if (thereIsConnection)
                numbersData
            else
                throw UnknownHostException()
        }

        override suspend fun randomNumberFact(): NumbersData {
            randomNumberFactCalledCount++
            return if (thereIsConnection)
                numbersData
            else
                throw UnknownHostException()
        }
    }

    private class TestNumbersCacheDataSource : NumbersCacheDataSource {
        val containsCalledList = mutableListOf<Boolean>()
        var numberFactCalled = mutableListOf<String>()
        var allNumbersCallCount = 0
        var saveNumberFactCalledCount = 0

        val data = mutableListOf<NumbersData>()

        fun replaceData(newData: List<NumbersData>): Unit = with(data) {
            clear()
            addAll(newData)
        }

        override suspend fun allNumbers(): List<NumbersData> {
            allNumbersCallCount++
            return data
        }

        override fun contains(number: String): Boolean {
            val result = data.find { it.id == number } != null
            containsCalledList.add(result)
            return result
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