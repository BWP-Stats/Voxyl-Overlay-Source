@file:Suppress("FunctionName")
@file:OptIn(ExperimentalComposeUiApi::class)

package com.voxyl.overlay.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.input.pointer.pointerMoveFilter as hoverable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.voxyl.overlay.Window
import com.voxyl.overlay.data.player.Tags
import com.voxyl.overlay.settings.Settings
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.mainview.MainSearchBar
import com.voxyl.overlay.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun TitleBar(fws: FrameWindowScope) {

    val cs = rememberCoroutineScope { Dispatchers.IO }
    var queriedName by remember { mutableStateOf(TextFieldValue()) }

    VoxylLogoForTitleBar()

    fws.WindowDraggableArea(modifier = Modifier.fillMaxWidth().height(64.dp).requestFocusOnClick())

    Row(
        Modifier
            .absolutePadding(top = 20.tbsm.dp)
            .fillMaxWidth()
    ) {
        MainColorSettingsButton()

        MainSearchBar(Modifier.weight(1f), queriedName, { queriedName = it }) {
            queriedName.text.split(" ").filterNot { it.isBlank() }.distinct().forEach {
                PlayerKindaButNotExactlyViewModel.add(it, cs, Tags.ManuallySearched)
            }
            queriedName = TextFieldValue()
        }

        RedCloseOverlayButton()

        YellowMinimizeButton()

        GreenAdditionalSettingsButton()
    }
}

@Composable
fun VoxylLogoForTitleBar() {
    Image(
        modifier = Modifier
            .size(60.tbsm.dp)
            .requestFocusOnClick(),
        painter = painterResource("VoxylLogoForTitleBar.png"),
        contentDescription = null,
        alpha = .8f.amf,
        colorFilter = ColorFilter.tint(MainColor.value.copy(alpha = 1f), BlendMode.SrcIn)
    )
}

@Composable
fun MainColorSettingsButton(modifier: Modifier = Modifier) = TitleBarButton(
    modifier = modifier.absolutePadding(left = 52.tbsm.dp),
    bgColor = mutableStateOf(MainColor.value),
    doOnClick = {
        ScreenShowing.screen = if (ScreenShowing.screen == "main") "settings" else "main"
    },
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
    modifier: Modifier = Modifier
) {
    val cs = rememberCoroutineScope()
    val alpha = if (AdditionalSettingsState.enabled) 235 else 160

    TitleBarButton(
        bgColor = mutableStateOf(Color(34, 197, 94, alpha).am),
        doOnClick = { },
        modifier = modifier.absolutePadding(right = 26.tbsm.dp)
            .hoverable(
                onEnter = {
                    AdditionalSettingsState.enabled = true
                    cs.launch {
                        titleBarSizeMulti.animateTo(1f)
                    }
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