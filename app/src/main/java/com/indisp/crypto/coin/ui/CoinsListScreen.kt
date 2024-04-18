package com.indisp.crypto.coin.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.indisp.designsystem.components.appbar.ToolBar
import com.indisp.designsystem.components.composable.LifecycleObserver
import com.indisp.designsystem.components.text.DsText
import com.indisp.designsystem.components.text.DsTextType
import com.indisp.designsystem.resource.Size
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "CoinsListScreen"

@Composable
fun CoinsListScreen(
    screenStateFlow: StateFlow<CoinsListViewModel.State>,
    sideEffectFlow: Flow<CoinsListViewModel.SideEffect>,
    processEvent: (CoinsListViewModel.Event) -> Unit,
    navigateToSearchScreen: () -> Unit
) {
    val state by screenStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LifecycleObserver(
        onCreate = { processEvent(CoinsListViewModel.Event.OnScreenCreated) }
    )

    LaunchedEffect(Unit) {
        sideEffectFlow.collectLatest {
            Log.d(TAG, "CoinsListScreen: -> $it")
            when (it) {
                is CoinsListViewModel.SideEffect.ShowError -> snackBarHostState.showSnackbar(message = it.error)
                CoinsListViewModel.SideEffect.NavigateToSearch -> {
                    processEvent(CoinsListViewModel.Event.OnNavigatedToSearchScreen)
                    navigateToSearchScreen()
                }
                CoinsListViewModel.SideEffect.Idle -> {}
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            if (state.isLoading)
                Loader()
            SearchBar { processEvent(CoinsListViewModel.Event.OnSearchClicked) }
            if (state.coinsList.isNotEmpty())
                CoinsList(coinsList = state.coinsList, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
private fun Loader() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}

@Composable
private fun SearchBar(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(Size.medium))
    ToolBar(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        DsText(
            text = "Search by coin name or symbol",
            type = DsTextType.Hint(imageVector = Icons.Default.Search),
            modifier = Modifier.padding(horizontal = Size.xLarge)
        )
    }
    Spacer(modifier = Modifier.height(Size.small))
}