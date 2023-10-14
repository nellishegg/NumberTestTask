package com.example.numbertesttask.numbers.data

import com.example.numbertesttask.numbers.domain.NumberFact

class NumberDataToDomain : NumbersData.Mapper<NumberFact> {
    override fun map(id: String, fact: String): NumberFact = NumberFact(id, fact)

}