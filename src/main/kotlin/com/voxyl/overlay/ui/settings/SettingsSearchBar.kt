package com.voxyl.overlay.ui.settings

import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.ui.elements.VTextField
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.theme.defaultTitleBarSizeMulti
import com.voxyl.overlay.ui.theme.tbsm
import com.voxyl.overlay.ui.theme.titleBarSizeMulti
import kotlinx.coroutines.launch

var queriedSetting by mutableStateOf(TextFieldValue())

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsSearchBar(
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var focused by remember { mutableStateOf(false) }
    val cs = rememberCoroutineScope()

    VTextField(
        value = queriedSetting,
        onValueChange = {
            queriedSetting = it
        },
        modifier = modifier
            .fillMaxWidth()
            .absoluteOffset(y = -16.dp)
            .padding(horizontal = 10.tbsm.dp)
            .height(50f.tbsm.dp)
            .onPreviewKeyEvent {
                when {
                    (it.key == Key.Escape && it.type == KeyEventType.KeyUp) -> {
                        focusManager.clearFocus()
                        true
                    }
                    else -> false
                }
            }
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
                text = "Search settings(s)",
                modifier = Modifier.absoluteOffset(y = 6.dp),
                fontSize = 10.sp
            )
        }
    )
}