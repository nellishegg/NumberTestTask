package com.example.numbertesttask.numbers.domain

import com.example.numbertesttask.numbers.presentation.NumberUi

class NumberUiMapper: NumberFact.Mapper<NumberUi> {
    override fun map(id: String, fact: String): NumberUi = NumberUi(id, fact)
}