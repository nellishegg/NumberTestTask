package com.example.numbertesttask.numbers.data

interface NumbersCacheDataSource : FetchNumber{
     suspend fun allNumbers(): List<NumbersData>

     suspend fun contains(number: String): Boolean


     suspend fun saveNumber(numberData: NumbersData)
}

interface FetchNumber{
     suspend fun number(number: String): NumbersData
}