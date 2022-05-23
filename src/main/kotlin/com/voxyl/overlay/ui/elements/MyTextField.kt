@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import com.voxyl.overlay.ui.theme.*

@Composable
fun MyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable() (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        unfocusedLabelColor = MainWhite.copy(alpha = .313f).am,
        focusedLabelColor = MainColor.value.copy(alpha = .94f).am,
        unfocusedIndicatorColor = MainColor.value,
        focusedIndicatorColor = MainColor.value.copy(alpha = .94f).am,
        cursorColor = MainColor.value.copy(alpha = .94f).am,
        textColor = MainWhite.copy(alpha = .94f).am,
        backgroundColor = Color.Transparent,
        errorLabelColor = ErrorColor,
        errorIndicatorColor = ErrorColor,
        errorCursorColor = ErrorColor,
    ),
    trailingIcon: @Composable() (() -> Unit)? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    textStyle: TextStyle = TextStyle(fontFamily = Fonts.nunito()),
    placeholder: @Composable() (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        colors = colors,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        isError = isError,
        textStyle = textStyle,
        placeholder = placeholder
    )
}

@Composable
fun MyTrailingIcon(
    icon: ImageVector = Icons.Filled.Done,
    tint: Color = MainWhite.copy(alpha = .313f).am,
    modifier: Modifier = Modifier,
    pointerIcon: PointerIcon = PointerIconDefaults.Hand,
    onClick: () -> Unit
) {
    Icon(
        icon,
        contentDescription = null,
        tint = tint,
        modifier = modifier
            .pointerHoverIcon(
                icon = pointerIcon
            )
            .clickable {
                onClick()
            }
    )
}

@ExperimentalComposeUiApi
@Composable
fun Modifier.onEnterOrEsc(
    focusManager: FocusManager,
    doOnEnter: () -> Unit,
    value: TextFieldValue,
    isValid: (TextFieldValue) -> Boolean
) = onPreviewKeyEvent {
    when {
        (it.key == Key.Enter && it.type == KeyEventType.KeyUp && isValid(value)) -> {
            doOnEnter()
            true
        }
        (it.key == Key.Escape && it.type == KeyEventType.KeyUp) -> {
            focusManager.clearFocus()
            true
        }
        else -> false
    }
}