package com.indisp.designsystem.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.indisp.designsystem.components.text.DsText
import com.indisp.designsystem.components.text.DsTextType

@Composable
fun DsButton(
    type: DsButtonType,
    onClick: () -> Unit
) {
    when (type) {
        is DsButtonType.Primary -> {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(1000.dp),
                onClick = onClick,
            ) {
                Text(
                    modifier = Modifier.padding(all = 8.dp),
                    text = type.text,
                )
            }
        }

        is DsButtonType.Chip -> {
            val border = BorderStroke(
                1.dp,
                if (type.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            val bgColor =
                if (type.isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = bgColor
                ),
                border = border,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                DsText(
                    text = type.text,
                    type = DsTextType.Secondary(imageVector = type.icon)
                )
            }
        }
    }
}

sealed class DsButtonType(
    open val text: String,
    open val icon: ImageVector? = null
) {
    data class Chip(
        override val text: String,
        override val icon: ImageVector? = null,
        val isSelected: Boolean = false
    ) : DsButtonType(text, icon)

    data class Primary(
        override val text: String,
        override val icon: ImageVector? = null
    ) : DsButtonType(text, icon)
}