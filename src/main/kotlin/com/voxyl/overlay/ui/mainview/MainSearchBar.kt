@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.mainview

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.ui.common.elements.MyTextField
import com.voxyl.overlay.ui.common.elements.MyTrailingIcon
import com.voxyl.overlay.ui.common.elements.onEnterOrEsc
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.theme.defaultTitleBarSizeMulti
import com.voxyl.overlay.ui.theme.tbsm
import com.voxyl.overlay.ui.theme.titleBarSizeMulti
import kotlinx.coroutines.launch

@Composable
fun MainSearchBar(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    doOnValueChange: (TextFieldValue) -> Unit,
    doOnEnter: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var focused by remember { mutableStateOf(false) }
    val cs = rememberCoroutineScope()

    MyTextField(
        value = value,
        onValueChange = {
            doOnValueChange(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .absoluteOffset(y = (-16).dp)
            .padding(horizontal = 10.tbsm.dp)
            .height(50f.tbsm.dp)
            .onEnterOrEsc(
                focusManager,
                doOnEnter,
                value
            ) { isValid(it) }
            .onFocusEvent {
                if (50f.tbsm > 50f) {
                    return@onFocusEvent
                }

                if (it.isFocused && !focused) {
                    cs.launch {
                        titleBarSizeMulti.animateTo(1f)
                    }
                    focused = true
                } else if (!it.isFocused && focused) {
                    cs.launch {
                        titleBarSizeMulti.animateTo(defaultTitleBarSizeMulti)
                    }
                    focused = false
                }
            },
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