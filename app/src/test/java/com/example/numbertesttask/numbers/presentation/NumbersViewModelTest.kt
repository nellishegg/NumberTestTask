package com.example.numbertesttask.numbers.presentation

import android.view.View
import com.example.numbertesttask.numbers.domain.NumberFact
import com.example.numbertesttask.numbers.domain.NumberUiMapper
import com.example.numbertesttask.numbers.domain.NumbersInteractor
import com.example.numbertesttask.numbers.domain.NumbersResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NumbersViewModelTest : BaseTest() {


    private lateinit var viewModel: NumbersViewModel
    private lateinit var communication: TestNumbersCommunications
    private lateinit var interactor: TestNumberInteractor
    private lateinit var manageResources: TestManageResources

    @Before
    fun init() {
        communication = TestNumbersCommunications()
        interactor = TestNumberInteractor()
        manageResources = TestManageResources()
        viewModel =
            NumbersViewModel(
                HandleNumbersRequest.Base(
                    TestDispatchersList(),
                    communication,
                    NumberResultMapper(communication, NumberUiMapper())
                ),
                manageResources,
                communication,
                interactor
            )
    }

    @Test
    fun `test init and re-init`() {
        interactor.changeExpectedResult(NumbersResult.Success())
        //2.action
        viewModel.init(isFirstRun = true)
        //3.check
        assertEquals(View.VISIBLE, communication.progressCalledList[0])
        assertEquals(1, interactor.initCalledList.size)

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(View.GONE, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(true, communication.stateCalledList[0] is UiState.Success)

        assertEquals(0, communication.numbersList.size)
        assertEquals(0, communication.timesShowList)

//get some data
        interactor.changeExpectedResult(NumbersResult.Failure("no internet connection"))
        viewModel.fetchRandoNumberFact()

        assertEquals(View.VISIBLE, communication.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)


        assertEquals(4, communication.progressCalledList.size)
        assertEquals(View.GONE, communication.progressCalledList[3])

        assertEquals(2, communication.stateCalledList.size)
        assertEquals(UiState.ShowError("no internet connection"), communication.stateCalledList[1])
        assertEquals(0, communication.timesShowList)

        viewModel.init(isFirstRun = false)
        //поворот экрана (data from livedata go right to the UI and saves the last(previous) state)
        assertEquals(4, communication.progressCalledList.size)
        assertEquals(2, communication.stateCalledList.size)
        assertEquals(0, communication.timesShowList)

    }

    /**
     * tru to get info about empty number
     */
    @Test
    fun `fact about empty number`() {
        manageResources.makeExpectedAnswer("entered number is empty")
        viewModel.fetchNumberFact("")

        assertEquals(0, interactor.fetchAboutNumberCalledList.size)

    }

    /**
     * tru to get info about some number
     */
    @Test
    fun `fact about some number`() {
        interactor.changeExpectedResult(
            NumbersResult.Success(listOf(NumberFact("45", "fact about 45")))
        )
        viewModel.fetchNumberFact("45")

        assertEquals(View.VISIBLE, communication.progressCalledList[0])

        assertEquals(1, interactor.fetchAboutNumberCalledList.size)

        assertEquals(
            NumbersResult.Success(listOf(NumberFact("45", "fact about 45"))),
            interactor.fetchAboutNumberCalledList[0]
        )

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(View.GONE, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(true, communication.stateCalledList[0] is UiState.Success)

        assertEquals(1, communication.timesShowList)
        assertEquals(NumberUi("45", "fact about 45"), communication.numbersList[0])
    }

    @Test
    fun `test clear error`(){
        viewModel.clearError()

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(true, communication.stateCalledList[0] is UiState.ClearError)

    }

    private class TestManageResources : ManageResources {

        private var string: String = ""
        fun makeExpectedAnswer(expected: String) {
            string = expected
        }

        override fun string(id: Int): String = string
    }
}

private class TestNumberInteractor : NumbersInteractor {

    private var result: NumbersResult = NumbersResult.Success()
    val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
    val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()
    val initCalledList = mutableListOf<NumbersResult>()

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
        fetchAboutRandomNumberCalledList.add(result)
        return result
    }
}

private class TestDispatchersList(
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
) : DispatchersList {

    override fun io(): CoroutineDispatcher = dispatcher
    override fun ui(): CoroutineDispatcher = dispatcher
}