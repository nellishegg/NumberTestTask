package com.example.numbertesttask.numbers.presentation

import com.example.numbertesttask.numbers.domain.NumbersInteractor
import com.example.numbertesttask.numbers.domain.NumbersResult
import org.junit.Assert.*
import org.junit.Test

class NumbersViewModelTest {
    /**
     * Initial test
     * At start fetch data and show it, then
     * then try to get some data
     * then re-init and wait for the result
     */

    @Test
    fun `test init and re-init`() {
        val communication = TestNumbersCommunications()
        val interactor = TestNumberInteractor()
        //1.init
        val viewModel = NumbersViewModel(/* dependencies*/communication, interactor)
        interactor.changeExpectedResult(NumbersResult.Success())
        //2.action
        viewModel.init(isFirstRun = true)
        //3.check
        assertEquals(1, communication.progressCalledList.size)
        assertEquals(true, communication.progressCalledList[0])

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(UiState.Success(), communication.stateCalledList[0])

        assertEquals(0, communication.numbersList.size)
        assertEquals(1, communication.timesShowList)


//get some data
        interactor.changeExpectedResult(NumbersResult.Failure())
        viewModel.fetchRandonmNumberData()

        assertEquals(3, communication.progressCalledList.size)
        assertEquals(true, communication.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)


        assertEquals(4, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[3])

        assertEquals(2, communication.stateCalledList.size)
        assertEquals(UiState.Error("entered number is empty"), communication.stateCalledList[1])
        assertEquals(1, communication.timesShowList)

        viewModel.init(isFirstRun = false)
        //поворот экрана (data from livedata go right to the UI and saves the last(previous) state)
        assertEquals(4, communication.progressCalledList.size)
        assertEquals(2, communication.stateCalledList.size)
        assertEquals(1, communication.timesShowList)

    }

    /**
     * tru to get info about empty number
     */
    @Test
    fun `fact about empty number`() {
        val communication = TestNumbersCommunications()
        val interactor = TestNumberInteractor()

        val viewModel = NumbersViewModel(/* dependencies*/communication, interactor)

        viewModel.fetchFact("")
        assertEquals(0, interactor.fetchAboutRandomCalledList.size)


        assertEquals(0, communication.progressCalledList.size)

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(UiState.Error("entered number is empty"), communication.stateCalledList[0])

        assertEquals(0, communication.timesShowList)
    }

    /**
     * tru to get info about some number
     */
    @Test
    fun `fact about some number`() {
        val communication = TestNumbersCommunications()
        val interactor = TestNumberInteractor()

        val viewModel = NumbersViewModel(/* dependencies*/communication, interactor)

        interactor.changeExpectedResult(NumbersResult.Success(listOf(Number("45", "fact about 45"))))
        viewModel.fetchFact("45")

        assertEquals(1, communication.progressCalledList.size)
        assertEquals(true, communication.progressCalledList[0])

        assertEquals(0, interactor.fetchAboutRandomNumberCalledList.size)
        assertEquals(NumberUi("45", "fact about 45"), interactor.fetchAboutNumberCalledList[0])

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(UiState.Success(), communication.stateCalledList[0])

        assertEquals(1, communication.timesShowList)
        assertEquals(NumberUi("45", "fact about 45"), communication.numbersList[0])
    }

    private class TestNumbersCommunications : NumbersController {

        val progressCalledList = mutableListOf<Boolean>()
        val stateCalledList = mutableListOf<Boolean>()
        var timesShowList = 0
        val numbersList = mutableListOf<NumberUi>()

        override fun showProgress(show: Boolean) {
            progressCalledList.add(show)
        }

        override fun showState(state: UiState) {
            stateCalledList.add(state)
        }

        override fun showList(list: List<NumberUi>) {
            timesShowList++
            numbersList.addAll(list)
        }
    }

    private class TestNumberInteractor : NumbersInteractor {

        private var result: NumbersResult = NumbersResult.Success()
        val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
        val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()

        fun changeExpectedResult(newResult: NumbersResult) {
            result = newResult
        }

        override suspend fun init(): NumbersResult {
            initCalledList.add(result)
            return result
        }

        override suspend fun factAboutNumber(number: String): NumbersResult {
            fetchAboutNumberCalledList.add(result)
            return result
        }

        override suspend fun factAboutRandomNumber(): NumbersResult {
            TODO("Not yet implemented")

        }

        override suspend fun factAboutRandomNumbers(): NumbersResult {
            fetchAboutRandomNumberCalledList.add(result)
            return result
        }
    }
}