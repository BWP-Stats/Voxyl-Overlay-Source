@file:Suppress("FunctionName")
@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.voxyl.overlay.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.Settings
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.main.elements.MainSearchBar
import kotlinx.coroutines.Dispatchers
import kotlin.system.exitProcess

@ExperimentalComposeUiApi
@Composable
fun TitleBar(settingsMenu: MutableState<Boolean>, additionalSettingsEnabled: MutableState<Boolean>) {

    val cs = rememberCoroutineScope { Dispatchers.IO }
    var queriedName by remember { mutableStateOf(TextFieldValue()) }

    Row(
        Modifier
            .absolutePadding(top = 20.dp)
            .fillMaxWidth()
    ) {
        PurpleSettingsButton(settingsMenu = settingsMenu)

        MainSearchBar(Modifier.weight(1f), queriedName, { queriedName = it }) {
            queriedName.text.split(" ").filterNot { it.isBlank() }.distinct().forEach {
                PlayerKindaButNotExactlyViewModel.add(it, cs)
            }
            queriedName = TextFieldValue()
        }

        RedCloseOverlayButton()

        YellowMinimizeButton()

        GreenAdditionalSettingsButton(enabled = additionalSettingsEnabled)
    }
}

@Composable
fun PurpleSettingsButton(modifier: Modifier = Modifier, settingsMenu: MutableState<Boolean>) = TitleBarButton(
    modifier = modifier.absolutePadding(left = 52.dp),
    bgColor = mutableStateOf(Color(130, 32, 229, 160)),
    doOnClick = { settingsMenu.value = !settingsMenu.value },
)

@Composable
fun RedCloseOverlayButton(modifier: Modifier = Modifier) = TitleBarButton(
    modifier = modifier.absolutePadding(right = 10.dp),
    bgColor = mutableStateOf(Color(190, 18, 60, 160)),
    doOnClick = {
        Settings.storeAll()
        exitProcess(0)
    }
)

@Composable
fun YellowMinimizeButton(modifier: Modifier = Modifier) = TitleBarButton(
    modifier = modifier.absolutePadding(right = 10.dp),
    bgColor = mutableStateOf(Color(251, 191, 36, 160)),
    doOnClick = {
        window.isMinimized = true
    }
)

@Composable
fun GreenAdditionalSettingsButton(
    modifier: Modifier = Modifier,
    enabled: MutableState<Boolean>,
) {
    val alpha = if (enabled.value) 235 else 160

    TitleBarButton(
        modifier = modifier.absolutePadding(right = 26.dp),
        bgColor = mutableStateOf(Color(34, 197, 94, alpha)),
        doOnClick = {
            enabled.value = !enabled.value
        }
    )
}

@Composable
fun TitleBarButton(
    modifier: Modifier = Modifier,
    bgColor: State<Color>,
    doOnClick: () -> Unit,
    content: @Composable RowScope.() -> Unit = { },
) = Button(
    modifier = modifier
        .size(28.dp)
        .requestFocusOnClick(),
    colors = object : ButtonColors {
        @Composable
        override fun backgroundColor(enabled: Boolean) = bgColor

        @Composable
        override fun contentColor(enabled: Boolean) = mutableStateOf(Color(220, 220, 220, 0))
    },
    onClick = doOnClick,
    content = content,
    shape = CircleShape,
    elevation = null
)