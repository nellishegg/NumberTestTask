package com.example.numbertesttask.numbers.sl

import com.example.numbertesttask.main.presentation.sl.Core
import com.example.numbertesttask.main.presentation.sl.Module
import com.example.numbertesttask.numbers.data.BaseNumbersRepository
import com.example.numbertesttask.numbers.data.HandleDataRequest
import com.example.numbertesttask.numbers.data.HandleDomainError
import com.example.numbertesttask.numbers.data.NumberDataToDomain
import com.example.numbertesttask.numbers.data.cache.NumberDataToCache
import com.example.numbertesttask.numbers.data.cache.NumbersCacheDataSource
import com.example.numbertesttask.numbers.data.cloud.NumbersCloudDataSource
import com.example.numbertesttask.numbers.data.cloud.NumbersService
import com.example.numbertesttask.numbers.domain.HandleError
import com.example.numbertesttask.numbers.domain.HandleRequest
import com.example.numbertesttask.numbers.domain.NumberUiMapper
import com.example.numbertesttask.numbers.domain.NumbersInteractor
import com.example.numbertesttask.numbers.presentation.HandleNumbersRequest
import com.example.numbertesttask.numbers.presentation.NumberResultMapper
import com.example.numbertesttask.numbers.presentation.NumbersCommunication
import com.example.numbertesttask.numbers.presentation.NumbersListCommunication
import com.example.numbertesttask.numbers.presentation.NumbersStateCommunication
import com.example.numbertesttask.numbers.presentation.NumbersViewModel
import com.example.numbertesttask.numbers.presentation.ProgressCommunication

class NumbersModule(private val core: Core) : Module<NumbersViewModel> {
    override fun viewModel(): NumbersViewModel {
        val communications = NumbersCommunication.Base(
            ProgressCommunication.Base(),
            NumbersStateCommunication.Base(),
            NumbersListCommunication.Base()
        )
        val cacheDataSource = NumbersCacheDataSource.Base(
            core.provideDataBase().numbersDao(),
            NumberDataToCache()
        )
        val repository = BaseNumbersRepository(
            NumbersCloudDataSource.Base(
                core.service(NumbersService::class.java)
            ),
            cacheDataSource,
            HandleDataRequest.Base(
                cacheDataSource,
                NumberDataToDomain(),
                HandleDomainError()
            ),
            NumberDataToDomain()
        )
        return NumbersViewModel(
            HandleNumbersRequest.Base(
                core.provideDispatchers(),
                communications,
                NumberResultMapper(communications, NumberUiMapper())
            ),
            core,
            communications,
            NumbersInteractor.Base(
                repository,
                HandleRequest.Base(
                    HandleError.Base(core),
                    repository
                )
            )
        )
    }
}
