@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.main.elements

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.ui.common.elements.MyTextField
import com.voxyl.overlay.ui.common.elements.MyTrailingIcon
import com.voxyl.overlay.ui.common.elements.onEnterOrEsc
import com.voxyl.overlay.viewelements.MyText

@Composable
fun MainSearchBar(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    doOnValueChange: (TextFieldValue) -> Unit,
    doOnEnter: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    MyTextField(
        value = value,
        onValueChange = {
            doOnValueChange(it)
        },
        modifier =
        modifier
            .fillMaxWidth()
            .absoluteOffset(y = (-16).dp)
            .padding(horizontal = 10.dp)
            .height(50.dp)
            .onEnterOrEsc(
                focusManager,
                doOnEnter,
                value,
                { isValid(it) }
            ),
        label = {
            MyText(
                text = if (isValid(value)) "Search player(s)" else "Invalid characters and/or name(s) exceeds 16 chars",
                modifier = Modifier.absoluteOffset(y = 6.dp)
            )
        },
        trailingIcon = {
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValid(value)) doOnEnter()
            }
        },
        isError = !isValid(value)
    )
}

fun isValid(value: TextFieldValue): Boolean {
    return value.text.matches("\\w{0,16}(?: +\\w{0,16})*".toRegex())
}