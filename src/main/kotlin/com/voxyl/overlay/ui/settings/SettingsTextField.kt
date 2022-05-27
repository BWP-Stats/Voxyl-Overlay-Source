package com.voxyl.overlay.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.ui.elements.VTextField
import com.voxyl.overlay.ui.elements.VTrailingIcon
import com.voxyl.overlay.ui.elements.onEnterOrEsc
import com.voxyl.overlay.ui.elements.VText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsTextField(
    text: String,
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onValueChange: (TextFieldValue) -> Unit,
    doOnEnter: () -> Unit,
    isValid: (TextFieldValue) -> Boolean = { true },
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current

    VTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp)
            .onEnterOrEsc(
                focusManager,
                doOnEnter,
                value,
                isValid
            ),
        label = {
            VText(
                text = text,
                modifier = Modifier.absoluteOffset(y = 6.dp),
                fontSize = 10.sp
            )
        },
        placeholder = {
            VText(placeholder, alpha = .313f)
        },
        onValueChange = {
            onValueChange(it)
        },
        trailingIcon = trailingIcon ?: {
            VTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValid(value)) doOnEnter()
            }
        },
        leadingIcon = leadingIcon,
        isError = !isValid(value)
    )
}
