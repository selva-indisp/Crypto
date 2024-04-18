package com.indisp.crypto.coin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indisp.crypto.R
import com.indisp.crypto.coin.ui.model.PresentableCoin
import com.indisp.designsystem.components.text.DsText
import com.indisp.designsystem.components.text.DsTextType
import com.indisp.designsystem.resource.Size
import kotlinx.collections.immutable.PersistentList

@Composable
fun CoinsList(coinsList: PersistentList<PresentableCoin>, modifier: Modifier) {
    Spacer(modifier = Modifier.height(Size.large))
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = coinsList.size
        ) {
            CoinsListItem(item = coinsList[it])
            if (it < coinsList.lastIndex)
                Divider()
        }
    }
}

@Composable
private fun CoinsListItem(item: PresentableCoin) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = Size.small)
        ) {
            DsText(
                text = item.name,
                type = DsTextType.Primary(),
                isDisabled = item.isActive.not(),
                modifier = Modifier.padding(horizontal = Size.medium)
            )
            Spacer(modifier = Modifier.height(Size.medium))
            DsText(
                text = item.symbol,
                type = DsTextType.Secondary(),
                isDisabled = item.isActive.not(),
                modifier = Modifier.padding(horizontal = Size.medium)
            )
        }
        Box(
            modifier = Modifier.size(60.dp)
        ) {
            if (item.isNew)
                Image(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp),
                    painter = painterResource(id = R.drawable.new_tag),
                    contentDescription = "",
                )
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = item.imageId),
                contentDescription = ""
            )
        }
    }
}
