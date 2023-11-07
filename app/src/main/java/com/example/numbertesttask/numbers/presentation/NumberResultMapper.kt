package com.example.numbertesttask.numbers.presentation

import com.example.numbertesttask.numbers.domain.NumberFact
import com.example.numbertesttask.numbers.domain.NumbersResult

class NumberResultMapper(
    private val communications: NumbersCommunication,
    private val mapper: NumberFact.Mapper<NumberUi>
) : NumbersResult.Mapper<Unit> {

    override fun map(list: List<NumberFact>, errorMessage: String) = communications.showState(
        if (errorMessage.isEmpty()) {
            if (list.isNotEmpty())
                communications.showList(list.map { it.map(mapper) })
            UiState.Success()
        } else
            UiState.ShowError(errorMessage)
    )
}