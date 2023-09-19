package com.example.numbertesttask.numbers.domain

import com.example.numbertesttask.numbers.presentation.ManageResources
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NumbersInteractorTest {

    private lateinit var interactor: NumbersInteractor
    private lateinit var repository: TestNumbersRepository
    private lateinit var manageResources: TestManageResources

    @Before
    fun setUp() {
        manageResources = TestManageResources()
        repository = TestNumbersRepository()
        interactor =
            NumbersInteractor.Base(
                repository,
                HandleRequest.Base(HandleError.Base(manageResources), repository)
            )
    }

    @Test
    fun test_init_success() = runBlocking {
        repository.changeExpectedList(listOf(NumberFact("6", "fact about 6")))

        val actual = interactor.init()
        val expected = NumbersResult.Success(listOf(NumberFact("6", "fact about 6")))

        assertEquals(expected, actual)
        assertEquals(1, repository.allNumberCalledCount)
    }

    @Test
    fun fact_about_number_success(): Unit = runBlocking {
        repository.changeExpectedFactOfNumber(NumberFact("7", "fact about 7"))

        val actual = interactor.factAboutNumber("7")
        val expected = NumbersResult.Success(listOf(NumberFact("7", "fact about number")))

        assertEquals(expected, actual)
        assertEquals("7", repository.numberFactCalledList[0])
        assertEquals(1, repository.numberFactCalledList.size)
    }

    @Test
    fun test_fact_about_number_error() = runBlocking {
        //prepare data
        repository.expectingErrorGetFact(true)
        manageResources.changeExpected("no internet connection")

        //action
        val actual = interactor.factAboutNumber("7")
        val expected = NumbersResult.Failure("no internet connection")

        //checking
        assertEquals(expected, actual)
        assertEquals("7", repository.numberFactCalledList[0])
        assertEquals(1, repository.numberFactCalledList.size)
    }

    @Test
    fun fact_about_random_number(): Unit = runBlocking {
        repository.changeExpectedFactOfRandomNumber(NumberFact("7", "fact about 7"))

        val actual = interactor.factAboutRandomNumber()
        val expected = NumbersResult.Success(listOf(NumberFact("7", "fact about number")))

        assertEquals(expected, actual)
        assertEquals(1, repository.randomNumberFactCalledList.size)
    }

    @Test
    fun test_fact_about_random_number_error() = runBlocking {
        //prepare data
        repository.expectingErrorGetRandomFact(true)
        manageResources.changeExpected("no internet connection")


        //action
        val actual = interactor.factAboutRandomNumber()
        val expected = NumbersResult.Failure("no internet connection")

        //checking
        assertEquals(expected, actual)
        assertEquals(1, repository.randomNumberFactCalledList.size)
    }

    class TestNumbersRepository : NumbersRepository {

        private val allNumbers = mutableListOf<NumberFact>()
        private var numberFact = NumberFact("", "")
        private var errorWhileNumberFact = false

        var allNumberCalledCount = 0
        val numberFactCalledList = mutableListOf<String>()
        val randomNumberFactCalledList = mutableListOf<String>()

        fun changeExpectedList(list: List<NumberFact>) {
            allNumbers.clear()
            allNumbers.addAll(list)
        }

        fun changeExpectedFactOfNumber(numberFact: NumberFact) {
            this.numberFact = numberFact
        }

        fun changeExpectedFactOfRandomNumber(numberFact: NumberFact) {
            this.numberFact = numberFact
        }

        fun expectingErrorGetFact(error: Boolean) {
            errorWhileNumberFact = error
        }

        fun expectingErrorGetRandomFact(error: Boolean) {
            errorWhileNumberFact = error
        }


        override suspend fun allNumbers(): List<NumberFact> {
            allNumberCalledCount++
            return allNumbers
        }

        override suspend fun numberFact(number: String): NumberFact {
            numberFactCalledList.add(number)
            if (errorWhileNumberFact)
                throw NoInternetConnectionException()
            return numberFact
        }

        override suspend fun randomNumberFact(): NumberFact {
            randomNumberFactCalledList.add("")

            if (errorWhileNumberFact)
                throw NoInternetConnectionException()
            return numberFact
        }

    }

    private class TestManageResources : ManageResources {

        private var value = ""
        fun changeExpected(string: String) {
            value = string
        }

        override fun string(id: Int): String = value
    }
}