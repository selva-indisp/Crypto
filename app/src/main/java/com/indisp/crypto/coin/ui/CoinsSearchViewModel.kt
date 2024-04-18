package com.indisp.crypto.coin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indisp.core.DispatcherProvider
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.model.CoinFilter
import com.indisp.crypto.coin.domain.usecase.GetCoinFiltersUseCase
import com.indisp.crypto.coin.domain.usecase.SearchCoinsByFilterUseCase
import com.indisp.crypto.coin.ui.model.PresentableCoin
import com.indisp.crypto.coin.ui.model.PresentableCoinFilter
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinsSearchViewModel(
    private val getCoinFiltersUseCase: GetCoinFiltersUseCase,
    private val searchCoinsByFilterUseCase: SearchCoinsByFilterUseCase,
    private val coinMapper: (List<Coin>) -> PersistentList<PresentableCoin>,
    private val filterMapper: (List<CoinFilter>) -> PersistentList<PresentableCoinFilter>,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {
    private companion object {
        val INITIAL_STATE = State(
            filteredCoinsList = persistentListOf(),
            filters = persistentListOf(),
            searchQuery = ""
        )
    }

    private val _searchQueryMutableFlow = MutableStateFlow("")
    private val _searchQueryFlow = _searchQueryMutableFlow.debounce(1000L).distinctUntilChanged()

    private val _searchFiltersFlow = MutableStateFlow(setOf<CoinFilter>())
    private val _searchQueryFiltersFlow = combine(_searchQueryFlow, _searchFiltersFlow) { query, filters ->
        filters + setOf(CoinFilter.Search(query))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptySet())

    private val _filteredCoinsListFlow = _searchQueryFiltersFlow.flatMapLatest { filters -> searchCoinsByFilterUseCase(filters) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptySet())

    private val _screenStateFlow = MutableStateFlow(INITIAL_STATE)
    val screenStateFlow = combine(_screenStateFlow, _filteredCoinsListFlow) { state, filteredCoins ->
        state.copy(
            filteredCoinsList = coinMapper(filteredCoins.toList())
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), INITIAL_STATE)

    private fun fetchCoinFilters() {
        viewModelScope.launch(dispatcherProvider.IO) {
            if (state().filters.isEmpty()) {
                _screenStateFlow.update { it.copy(filters = filterMapper(getCoinFiltersUseCase())) }
            }
        }
    }

    private fun searchQuery(query: String) {
        _screenStateFlow.update { it.copy(searchQuery = query) }
        _searchQueryMutableFlow.update { query }
    }

    private fun applyFilter(filter: PresentableCoinFilter) {
        viewModelScope.launch(dispatcherProvider.IO) {
            _screenStateFlow.update { state ->
                state.copy(
                    filters = state.filters.map { if (it == filter) it.copy(isSelected = !it.isSelected) else it }.toPersistentList()
                )
            }

            _searchFiltersFlow.update {
                state().filters.filter { it.isSelected }.map { it.filter }.toSet()
            }
        }
    }

    private fun state() = _screenStateFlow.value

    fun processEvent(event: Event) {
        when (event) {
            Event.OnScreenCreated -> fetchCoinFilters()
            is Event.OnSearchQueryChanged -> searchQuery(event.query)
            is Event.OnFilterToggled -> applyFilter(event.filter)
        }
    }

    data class State(
        val filteredCoinsList: PersistentList<PresentableCoin>,
        val filters: PersistentList<PresentableCoinFilter>,
        val searchQuery: String
    )

    sealed interface Event {
        object OnScreenCreated: Event
        data class OnSearchQueryChanged(val query: String): Event
        data class OnFilterToggled(val filter: PresentableCoinFilter): Event
    }
}