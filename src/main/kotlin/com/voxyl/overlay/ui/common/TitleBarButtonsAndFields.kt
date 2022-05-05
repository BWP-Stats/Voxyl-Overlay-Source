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
import androidx.compose.ui.input.pointer.pointerMoveFilter as hoverable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.Window
import com.voxyl.overlay.settings.Settings
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.mainview.MainSearchBar
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.tbsm
import com.voxyl.overlay.ui.theme.titleBarSizeMulti
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@ExperimentalComposeUiApi
@Composable
fun TitleBarButtonsAndFields(
    settingsMenuToggled: MutableState<Boolean>,
    additionalSettingsEnabled: MutableState<Boolean>
) {

    val cs = rememberCoroutineScope { Dispatchers.IO }
    var queriedName by remember { mutableStateOf(TextFieldValue()) }

    Row(
        Modifier
            .absolutePadding(top = 20.tbsm.dp)
            .fillMaxWidth()
    ) {
        PurpleSettingsButton(settingsMenu = settingsMenuToggled)

        MainSearchBar(Modifier.weight(1f), queriedName, { queriedName = it }) {
            queriedName.text.split(" ").filterNot { it.isBlank() }.distinct().forEach {
                PlayerKindaButNotExactlyViewModel.add(it, cs)
            }
            queriedName = TextFieldValue()
        }

        RedCloseOverlayButton()

        YellowMinimizeButton()

        GreenAdditionalSettingsButton(additionalSettingsEnabled)
    }
}

@Composable
fun PurpleSettingsButton(modifier: Modifier = Modifier, settingsMenu: MutableState<Boolean>) = TitleBarButton(
    modifier = modifier.absolutePadding(left = 52.tbsm.dp),
    bgColor = mutableStateOf(Color(130, 32, 229, 160).am),
    doOnClick = { settingsMenu.value = !settingsMenu.value },
)

@Composable
fun RedCloseOverlayButton(modifier: Modifier = Modifier) = TitleBarButton(
    modifier = modifier.absolutePadding(right = 10.tbsm.dp),
    bgColor = mutableStateOf(Color(190, 18, 60, 160).am),
    doOnClick = {
        Settings.storeAll()
        exitProcess(0)
    }
)

@Composable
fun YellowMinimizeButton(modifier: Modifier = Modifier) = TitleBarButton(
    modifier = modifier.absolutePadding(right = 10.tbsm.dp),
    bgColor = mutableStateOf(Color(251, 191, 36, 160).am),
    doOnClick = {
        Window.isMinimized = true
    }
)

@Composable
fun GreenAdditionalSettingsButton(
    enabled: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val cs = rememberCoroutineScope()
    val alpha = if (enabled.value) 235 else 160

    TitleBarButton(
        bgColor = mutableStateOf(Color(34, 197, 94, alpha).am),
        doOnClick = { },
        modifier = modifier.absolutePadding(right = 26.tbsm.dp)
            .hoverable(
                onEnter = {
                    enabled.value = true
                    cs.launch {
                        titleBarSizeMulti.animateTo(1f)
                    }
                    true
                },
                onExit = {
                    enabled.value = false
                    true
                }
            ),
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
        .size(28.tbsm.dp)
        .requestFocusOnClick(),
    colors = object : ButtonColors {
        @Composable
        override fun backgroundColor(enabled: Boolean) = bgColor

        @Composable
        override fun contentColor(enabled: Boolean) = mutableStateOf(Color(220, 220, 220, 0).am)
    },
    onClick = doOnClick,
    content = content,
    shape = CircleShape,
    elevation = null
)