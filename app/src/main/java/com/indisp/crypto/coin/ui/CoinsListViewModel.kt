package com.indisp.crypto.coin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indisp.core.DispatcherProvider
import com.indisp.core.Result
import com.indisp.crypto.coin.domain.model.Coin
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListError
import com.indisp.crypto.coin.domain.usecase.FetchCoinsListUseCase
import com.indisp.crypto.coin.ui.model.PresentableCoin
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinsListViewModel(
    private val fetchCoinsListUseCase: FetchCoinsListUseCase,
    private val coinMapper: (List<Coin>) -> PersistentList<PresentableCoin>,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private companion object {
        val INITIAL_SCREEN_STATE = State(
            coinsList = persistentListOf(), isLoading = false
        )
    }

    private val _sideEffectFlow = MutableStateFlow<SideEffect>(SideEffect.Idle)
    val sideEffectFlow = _sideEffectFlow.asStateFlow()
    private val _screenStateFlow = MutableStateFlow(INITIAL_SCREEN_STATE)
    val screenStateFlow = _screenStateFlow.asStateFlow()

    fun processEvent(event: Event) {
        when (event) {
            Event.OnScreenCreated -> {
                if (state().coinsList.isNotEmpty())
                    return
                viewModelScope.launch(dispatcherProvider.IO) {
                    _screenStateFlow.update { it.copy(isLoading = true) }
                    when (val result = fetchCoinsListUseCase()) {
                        is Result.Error -> handleFetchCoinsListError(result.error)
                        is Result.Success -> handleFetchCoinsListSuccess(result.data)
                    }
                }
            }

            Event.OnSearchClicked -> _sideEffectFlow.update { SideEffect.NavigateToSearch }
            Event.OnNavigatedToSearchScreen -> _sideEffectFlow.update { SideEffect.Idle }
        }
    }

    private fun handleFetchCoinsListError(result: FetchCoinsListError) {
        when (result) {
            is FetchCoinsListError.NetworkError -> {
                _screenStateFlow.update {
                    it.copy(
                        coinsList = coinMapper(result.cachedData), isLoading = false
                    )
                }
                _sideEffectFlow.update { SideEffect.ShowError(error = "Network error. Please try again!") } //TODO: Move string to resource
            }
        }
    }

    private fun handleFetchCoinsListSuccess(result: List<Coin>) {
        _screenStateFlow.update {
            it.copy(
                coinsList = coinMapper(result), isLoading = false
            )
        }
    }

    private fun state(): State = _screenStateFlow.value

    data class State(
        val coinsList: PersistentList<PresentableCoin>, val isLoading: Boolean
    )

    sealed interface Event {
        object OnScreenCreated : Event
        object OnSearchClicked : Event
        object OnNavigatedToSearchScreen: Event
    }

    sealed interface SideEffect {
        object Idle : SideEffect
        object NavigateToSearch : SideEffect
        data class ShowError(val error: String) : SideEffect
    }
}