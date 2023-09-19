package com.example.numbertesttask.numbers.domain

import java.lang.IllegalStateException


abstract class DomainException: IllegalStateException()

class NoInternetConnectionException: DomainException()

class ServiceUnavailableException: DomainException()
