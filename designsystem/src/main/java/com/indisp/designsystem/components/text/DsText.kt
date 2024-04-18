package com.indisp.designsystem.components.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indisp.designsystem.resource.Size

@Composable
fun DsText(
    text: String,
    type: DsTextType,
    align: TextAlign? = TextAlign.Start,
    isDisabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    var color = when (type) {
        DsTextType.Title,
        is DsTextType.Primary -> MaterialTheme.colorScheme.onBackground
        DsTextType.Error -> MaterialTheme.colorScheme.error
        DsTextType.Ternary -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        is DsTextType.Secondary -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        is DsTextType.Hint -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    }

    if (isDisabled)
        color = color.copy(alpha = 0.3f)

    if (type.prefixIcon != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = type.prefixIcon!!,
                contentDescription = "prefixIcon",
                tint = color,
                modifier = Modifier
                    .padding(top = Size.xSmall, end = Size.small)
                    .size(20.dp)
            )

            Text(
                text = text,
                fontSize = type.size,
                color = color,
                fontWeight = type.weight,
                letterSpacing = 0.5.sp,
                textAlign = align,
            )
        }
    } else {
        Text(
            text = text,
            fontSize = type.size,
            color = color,
            fontWeight = type.weight,
            letterSpacing = 1.sp,
            textAlign = align,
            modifier = modifier
        )
    }
}

sealed class DsTextType(
    open val size: TextUnit,
    open val weight: FontWeight,
    open val prefixIcon: ImageVector? = null,
    open val highlight: Boolean = false
) {

    object Title : DsTextType(
        size = 24.sp,
        weight = FontWeight.Normal
    )

    data class Primary(
        override val highlight: Boolean = false,
        val imageVector: ImageVector? = null
    ) : DsTextType(
        size = 16.sp,
        weight = if (highlight) FontWeight.Bold else FontWeight.Normal
    )

    data class Secondary(
        val imageVector: ImageVector? = null,
        val resourceId: Int? = null
    ) : DsTextType(
        size = 15.sp,
        weight = FontWeight.Normal,
        prefixIcon = imageVector
    )

    object Ternary : DsTextType(
        size = 12.sp,
        weight = FontWeight.Normal
    )

    object Error : DsTextType(
        size = 15.sp,
        weight = FontWeight.Normal
    )

    data class Hint(val imageVector: ImageVector? = null) : DsTextType(
        size = 16.sp,
        weight = FontWeight.Normal,
        prefixIcon = imageVector
    )
}