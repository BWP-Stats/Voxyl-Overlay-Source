@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.playerstats

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.business.player.tags.ManuallySearched
import com.voxyl.overlay.kindasortasomewhatviewmodelsishiguessithinkidkwhatevericantbebotheredsmh.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.elements.MyTextField
import com.voxyl.overlay.ui.elements.MyTrailingIcon
import com.voxyl.overlay.ui.elements.onEnterOrEsc
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.defaultTitleBarSizeMulti
import com.voxyl.overlay.ui.theme.tbsm
import com.voxyl.overlay.ui.theme.titleBarSizeMulti
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@Composable
fun PlayerSearchBar(
    modifier: Modifier = Modifier
) {
    var queriedName by remember { mutableStateOf(TextFieldValue()) }

    val focusManager = LocalFocusManager.current

    var focused by remember { mutableStateOf(false) }
    val cs = rememberCoroutineScope()

    val doOnEnter = {
        try {
            val savedQueriedName = queriedName.text

            queriedName = TextFieldValue()

            savedQueriedName.split(" ").filterNot { it.isBlank() }.distinct().forEach {
                PlayerKindaButNotExactlyViewModel.add(it, cs, ManuallySearched)
            }
        } catch (e: Exception) {
            Napier.wtf(e) { "Error adding someone to the KindaButNotExactlyViewModel" }
        }
    }


    MyTextField(
        value = queriedName,
        onValueChange = {
            queriedName = it
        },
        modifier = modifier
            .fillMaxWidth()
            .absoluteOffset(y = -16.dp)
            .padding(horizontal = 10.tbsm.dp)
            .height(50f.tbsm.dp)
            .onEnterOrEsc(
                focusManager,
                doOnEnter,
                queriedName
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
            VText(
                text = if (isValid(queriedName)) "Search player(s)" else "Invalid characters and/or name(s) exceeds 16 chars",
                modifier = Modifier.absoluteOffset(y = 6.dp),
                fontSize = 10.sp
            )
        },
        trailingIcon = {
            MyTrailingIcon(
                modifier = Modifier
                    .offset(x = 10.dp, y = 5.dp)
                    .size(12.dp, 12.dp)
            ) {
                if (isValid(queriedName)) doOnEnter()
            }
        },
        isError = !isValid(queriedName)
    )
}

fun isValid(value: TextFieldValue): Boolean {
    return value.text.matches("\\w{0,16}(?: +\\w{0,16})*".toRegex())
}