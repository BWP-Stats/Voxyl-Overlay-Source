@file:Suppress("FunctionName")
@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.voxyl.overlay.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.common.elements.ShapeThatIdkTheNameOf
import com.voxyl.overlay.ui.main.elements.MainSearchBar
import com.voxyl.overlay.ui.theme.MainWhite
import kotlinx.coroutines.Dispatchers
import kotlin.system.exitProcess

@ExperimentalComposeUiApi
@Composable
fun TitleBar(settingsMenu: MutableState<Boolean>) {

    val cs = rememberCoroutineScope { Dispatchers.IO }
    var queriedName by remember { mutableStateOf(TextFieldValue()) }

    var additionalSettingsEnabled by remember { mutableStateOf(false) }
    val animatedSize by animateDpAsState(
        targetValue = if (additionalSettingsEnabled) 170.dp else 0.dp
    )

    Row(
        Modifier
            .absolutePadding(top = 20.dp)
            .fillMaxWidth()
    ) {
        SettingsButton(settingsMenu = settingsMenu)

        MainSearchBar(Modifier.weight(1f), queriedName, { queriedName = it }) {
            queriedName.text.split(" ").filterNot { it.isBlank() }.distinct().forEach {
                PlayerKindaButNotExactlyViewModel.add(it, cs)
            }
            queriedName = TextFieldValue()
        }

        CloseOverlayButton()
        AlwaysOnTopButton()
        AdditionalThingsButton(additionalSettingsEnabled = additionalSettingsEnabled) {
            additionalSettingsEnabled = !additionalSettingsEnabled
        }
    }

    Box(
        Modifier
            .absolutePadding(top = 19.dp)
            .height(animatedSize)
            .fillMaxWidth()
    ) {
        if (animatedSize.value > 28) {
            AdditionalSettings(Modifier.align(Alignment.CenterEnd).absolutePadding(right = 25.dp)) {
                Column(
                    Modifier.absolutePadding(top = 32.dp, left = 3.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = null, tint = MainWhite)
                    Spacer(Modifier.height(3.dp))
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = MainWhite)
                    Spacer(Modifier.height(3.dp))
                    Icon(Icons.Filled.Refresh, contentDescription = null, tint = MainWhite)
                    Spacer(Modifier.height(3.dp))
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MainWhite)
                    Spacer(Modifier.height(3.dp))
                    Icon(Icons.Filled.Info, contentDescription = null, tint = MainWhite)
                }
            }
        }
    }
}


@Composable
fun AdditionalSettings(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .width(30.dp)
            .height(170.dp)
            .graphicsLayer {
                shape = ShapeThatIdkTheNameOf(Size(30f, 108f))
                clip = true
            }
            .background(color = Color(0, 0, 0, 164))
    ) {
        content()
    }
}

@Composable
fun SettingsButton(modifier: Modifier = Modifier, settingsMenu: MutableState<Boolean>) = TitleBarButton(

    modifier = modifier.absolutePadding(left = 52.dp),
    bgColor = mutableStateOf(Color(130, 32, 229, 160)),
    doOnClick = { settingsMenu.value = !settingsMenu.value },
)

@Composable
fun CloseOverlayButton(modifier: Modifier = Modifier) = TitleBarButton(

    modifier = modifier.absolutePadding(right = 10.dp),
    bgColor = mutableStateOf(Color(190, 18, 60, 160)),
    doOnClick = { exitProcess(0) }
)

@Composable
fun AlwaysOnTopButton(modifier: Modifier = Modifier) {

    var alpha by rememberSaveable { mutableStateOf(160) }

    TitleBarButton(
        modifier = modifier.absolutePadding(right = 10.dp),
        bgColor = mutableStateOf(Color(251, 191, 36, alpha)),
        doOnClick = {
            window.isAlwaysOnTop = !(window.isAlwaysOnTop)
            alpha = if (alpha == 160) 240 else 160
        }
    )
}

@Composable
fun AdditionalThingsButton(modifier: Modifier = Modifier, additionalSettingsEnabled: Boolean, onClick: () -> Unit) {

    val alpha = if (additionalSettingsEnabled) 235 else 160

    TitleBarButton(
        modifier = modifier.absolutePadding(right = 26.dp),
        bgColor = mutableStateOf(Color(34, 197, 94, alpha)),
        doOnClick = {
            onClick()
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