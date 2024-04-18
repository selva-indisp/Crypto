package com.indisp.designsystem.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.indisp.designsystem.components.text.DsText
import com.indisp.designsystem.components.text.DsTextType
import com.indisp.designsystem.resource.Size

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DsTextField(
    modifier: Modifier,
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    inputType: DsTextInputType,
    enabled: Boolean = true,
    focusRequester: FocusRequester = remember { FocusRequester() },
    prefix: @Composable () -> Unit = {},
    onDone: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardOptions = remember(inputType) {
        when (inputType) {
            DsTextInputType.PhoneNumber -> KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )

            DsTextInputType.Search -> KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            )
        }
    }

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = remember {
        TextStyle.Default.copy(
            color = textColor,
            fontSize = TextUnit(16F, TextUnitType.Sp),
            letterSpacing = TextUnit(1F, TextUnitType.Sp),
            textAlign = TextAlign.Left
        )
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(Size.xLarge),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        textStyle = textStyle,
        enabled = enabled,
        placeholder = { DsText(text = hint, type = DsTextType.Hint() )},
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDone()
            },
            onSearch = {
                keyboardController?.hide()
                onDone()
            }
        ),
        prefix = prefix
    )
}

sealed class DsTextInputType {
    object PhoneNumber : DsTextInputType()
    object Search : DsTextInputType()
}