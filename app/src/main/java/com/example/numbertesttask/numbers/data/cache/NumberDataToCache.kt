package com.example.numbertesttask.numbers.data.cache

import com.example.numbertesttask.numbers.data.NumbersData

class NumberDataToCache: NumbersData.Mapper<NumberCache> {
    override fun map(id: String, fact: String) = NumberCache (id,fact,System.currentTimeMillis())
}