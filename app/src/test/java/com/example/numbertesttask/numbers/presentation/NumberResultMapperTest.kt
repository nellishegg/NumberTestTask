package com.example.numbertesttask.numbers.presentation

import com.example.numbertesttask.numbers.domain.NumberFact
import com.example.numbertesttask.numbers.domain.NumberUiMapper
import org.junit.Assert.*
import org.junit.Test

class NumberResultMapperTest : BaseTest() {
    //3 tests
    @Test
    fun test_error() {
        val communications = TestNumbersCommunications()
        val mapper = NumberResultMapper(communications, NumberUiMapper())

        mapper.map(emptyList(), "not empty message")

        assertEquals(UiState.Error("not empty message"), communications.stateCalledList[0])
    }

    @Test
    fun test_success_no_list() {
        val communications = BaseTest.TestNumbersCommunications()
        val mapper = NumberResultMapper(communications, NumberUiMapper())

        mapper.map(emptyList(), "not empty message")

        assertEquals(0, communications.timesShowList)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)

    }

    @Test
    fun test_success_with_list() {
        val communications = TestNumbersCommunications()
        val mapper = NumberResultMapper(communications, NumberUiMapper())

        mapper.map(listOf(NumberFact("5", "fact 5")), "")

        assertEquals(true, communications.stateCalledList[0] is UiState.Success)
        assertEquals(NumberUi("5", "fact 5"), communications.numbersList[0])

    }

}