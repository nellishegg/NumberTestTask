package com.example.numbertesttask.numbers.data

import com.example.numbertesttask.numbers.domain.HandleError
import com.example.numbertesttask.numbers.domain.NoInternetConnectionException
import com.example.numbertesttask.numbers.domain.ServiceUnavailableException
import java.net.UnknownHostException

class HandleDomainError:HandleError<Exception> {
    override fun handle(e: Exception) = when(e){
            is UnknownHostException -> NoInternetConnectionException()
            else-> ServiceUnavailableException()
        }
    }
