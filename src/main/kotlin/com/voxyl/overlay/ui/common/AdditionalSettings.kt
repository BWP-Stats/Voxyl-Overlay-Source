package com.voxyl.overlay.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.Window
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.settings.window.SavedWindowStateKeys.IsAlwaysOnTop
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.elements.ShapeThatIdkTheNameOf
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.theme.am
import com.voxyl.overlay.ui.theme.defaultTitleBarSizeMulti
import com.voxyl.overlay.ui.theme.titleBarSizeMulti
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.input.pointer.pointerMoveFilter as hoverable

@ExperimentalComposeUiApi
@Composable
fun AdditionalSettings(
    additionalSettingsEnabled: MutableState<Boolean>
) {
    //var prevMulti by remember { mutableStateOf(0f) }
    val cs = rememberCoroutineScope()
    val animatedSize by animateDpAsState(
        targetValue = if (additionalSettingsEnabled.value) 190.dp else 0.dp
    )

    Box(
        Modifier
            .absolutePadding(top = 19.dp)
            .height(animatedSize)
            .hoverable(
                onEnter = {
                    additionalSettingsEnabled.value = true
                    cs.launch {
                        titleBarSizeMulti.animateTo(1f)
                    }
                    true
                },
                onMove = {
                    if (it.x < Window.size.width - 60 || it.y > 170) {
                        cs.launch {
                            delay(130)
                            titleBarSizeMulti.animateTo(defaultTitleBarSizeMulti)
                        }
                        additionalSettingsEnabled.value = false
                    }
                    false
                }
            )
            .fillMaxWidth()
    ) {
        if (animatedSize.value < 28) return@Box

        AdditionalSettingsBox(Modifier.align(Alignment.CenterEnd).absolutePadding(right = 25.dp)) {
            Column(
                Modifier.absolutePadding(top = 32.dp, left = 3.dp),
                verticalArrangement = Arrangement.Center
            ) {
                AlwaysOnTopButton()

                Spacer(Modifier.height(3.dp))

                ClearPlayersButton()

                Spacer(Modifier.height(3.dp))

                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = MainWhite)

                Spacer(Modifier.height(3.dp))

                RefreshAllButton()

                Spacer(Modifier.height(3.dp))

                Icon(Icons.Filled.Info, contentDescription = null, tint = MainWhite)
            }
        }
    }
}

@Composable
fun AlwaysOnTopButton() {
    var tint by remember { mutableStateOf(if (Window.isAlwaysOnTop) MainWhite.copy(alpha = .99f).am else MainWhite) }

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        0f,
        360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Restart
        )
    )

    Icon(
        Icons.Filled.Star, contentDescription = null, tint = tint,
        modifier = Modifier
            .rotate(if (Window.isAlwaysOnTop) rotation else 0f)
            .clickable {
                Window.isAlwaysOnTop = !(Window.isAlwaysOnTop)
                SavedWindowState[IsAlwaysOnTop] = Window.isAlwaysOnTop.toString()
                tint = if (Window.isAlwaysOnTop) MainWhite.copy(alpha = .9f).am else MainWhite
            }
    )
}

@Composable
fun ClearPlayersButton() {
    Icon(
        Icons.Filled.Clear, contentDescription = null, tint = MainWhite,
        modifier = Modifier.clickable {
            PlayerKindaButNotExactlyViewModel.clear()
        }
    )
}

@Composable
fun RefreshAllButton() {
    val cs = rememberCoroutineScope()

    Icon(
        Icons.Filled.Refresh, contentDescription = null, tint = MainWhite,
        modifier = Modifier.clickable {
            PlayerKindaButNotExactlyViewModel.refreshAll(cs)
        }
    )
}

@Composable
fun AdditionalSettingsBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .width(30.dp)
            .height(170.dp)
            .graphicsLayer {
                shape = ShapeThatIdkTheNameOf(Size(30f, 108f))
                clip = true
            }
            .background(color = Color(0, 0, 0, 164).am)
            .requestFocusOnClick()
    ) {
        content()
    }
}