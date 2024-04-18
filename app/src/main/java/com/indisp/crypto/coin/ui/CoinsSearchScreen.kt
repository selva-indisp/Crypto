package com.indisp.crypto.coin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.indisp.crypto.coin.ui.model.PresentableCoinFilter
import com.indisp.designsystem.components.button.DsButton
import com.indisp.designsystem.components.button.DsButtonType
import com.indisp.designsystem.components.composable.LifecycleObserver
import com.indisp.designsystem.components.textfield.DsSearchField
import com.indisp.designsystem.resource.Size
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "CoinsSearchScreen"

@Composable
fun CoinsSearchScreen(
    screenStateFlow: StateFlow<CoinsSearchViewModel.State>,
    processEvent: (CoinsSearchViewModel.Event) -> Unit,
    navigateBack: () -> Unit
) {

    val state by screenStateFlow.collectAsState()

    LifecycleObserver(
        onCreate = { processEvent(CoinsSearchViewModel.Event.OnScreenCreated) }
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            DsSearchField(
                modifier = Modifier.padding(start = Size.medium, top = Size.large, end = Size.medium),
                value = state.searchQuery,
                onValueChange = { query ->
                    processEvent(CoinsSearchViewModel.Event.OnSearchQueryChanged(query))
                },
                onBackPressed = navigateBack
            )
            Filters(
                modifier = Modifier.padding(horizontal = Size.medium, vertical = Size.medium),
                filters = state.filters,
                onClick = { processEvent(CoinsSearchViewModel.Event.OnFilterToggled(it))}
            )
            if (state.filteredCoinsList.isNotEmpty())
                CoinsList(coinsList = state.filteredCoinsList, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
private fun Filters(
    modifier: Modifier,
    filters: PersistentList<PresentableCoinFilter>,
    onClick: (PresentableCoinFilter) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(
            count = filters.size,
        ) {
            DsButton(
                type = DsButtonType.Chip(
                    text = filters[it].text,
                    isSelected = filters[it].isSelected
                )
            ) { onClick(filters[it]) }
            Spacer(modifier = Modifier.width(Size.medium))
        }
    }
}