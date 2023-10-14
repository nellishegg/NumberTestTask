package com.example.numbertesttask.numbers.data

import java.net.UnknownHostException

interface NumbersCloudDataSource :FetchNumber{
    suspend fun randomNumber(): NumbersData
}