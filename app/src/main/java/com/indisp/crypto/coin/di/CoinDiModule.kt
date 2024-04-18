package com.indisp.crypto.coin.di

import com.indisp.core.DefaultDispatcher
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListUseCase
import com.indisp.crypto.coin.domain.usecase.GetCoinFiltersUseCase
import com.indisp.crypto.coin.domain.usecase.SearchCoinsByFilterUseCase
import com.indisp.crypto.coin.ui.CoinsListViewModel
import com.indisp.crypto.coin.ui.CoinsSearchViewModel
import com.indisp.crypto.coin.ui.mapper.mapToPresentableCoin
import com.indisp.crypto.coin.ui.mapper.mapToPresentableFilter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coinDiModule = module {
    factory<FetchCoinsListUseCase> { FetchCoinsListUseCase(get()) }
    factory<SearchCoinsByFilterUseCase> { SearchCoinsByFilterUseCase(get()) }
    factory<GetCoinFiltersUseCase> { GetCoinFiltersUseCase() }
    viewModel { CoinsListViewModel(get(), ::mapToPresentableCoin, DefaultDispatcher) }
    viewModel { CoinsSearchViewModel(get(), get(), ::mapToPresentableCoin, ::mapToPresentableFilter, DefaultDispatcher) }
}