package com.voxyl.overlay.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import com.voxyl.overlay.AppWindow
import com.voxyl.overlay.business.settings.window.*
import com.voxyl.overlay.controllers.common.ui.*
import com.voxyl.overlay.controllers.playerstats.Entities
import com.voxyl.overlay.ui.elements.ShapeThatIdkTheNameOf
import com.voxyl.overlay.ui.elements.Tooltip
import com.voxyl.overlay.ui.elements.util.requestFocusOnClick
import kotlinx.coroutines.launch
import androidx.compose.ui.input.pointer.pointerMoveFilter as hoverable

object AdditionalSettingsState {
    var enabled by mutableStateOf(false)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AdditionalSettings(
    modifier: Modifier = Modifier,
) {
    val cs = rememberCoroutineScope()
    val boxOffset = remember { Animatable(0f) }
    val animatedSize = remember { Animatable(0f) }

    LaunchedEffect(AdditionalSettingsState.enabled) {
        if (AdditionalSettingsState.enabled) {
            launch { boxOffset.animateTo(10f) }
            animatedSize.animateTo(190f)
        } else {
            launch { boxOffset.animateTo(17.tbsm) }
            animatedSize.animateTo(0f)
        }
    }

    Box(
        modifier
            .padding(vertical = boxOffset.value.dp)
            .height(animatedSize.value.dp)
            .hoverable({
                if (it.x < AppWindow.size.width - 60 || it.y > 170) {
                    cs.launch {
                        titleBarSizeMulti.animateTo(defaultTitleBarSizeMulti)
                    }
                    AdditionalSettingsState.enabled = false
                }
                false
            })
            .fillMaxWidth()
    ) {
        if (animatedSize.value < 28.tbsm) return@Box

        AdditionalSettingsBox(Modifier.align(Alignment.CenterEnd).absolutePadding(right = 25.tbsm.dp)) {
            Column(
                Modifier.absolutePadding(top = 32.tbsm.dp, left = 3.tbsm.dp),
                verticalArrangement = Arrangement.Center
            ) {
                AlwaysOnTopButton()

                Spacer(modifier = Modifier.size(3.dp))

                ClearPlayersButton()

                Spacer(modifier = Modifier.size(3.dp))

                Tooltip(
                    "Presets (coming soon)"
                ) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = MainWhite)
                }

                Spacer(modifier = Modifier.size(3.dp))

                RefreshAllButton()

                Spacer(modifier = Modifier.size(3.dp))

                Tooltip(
                    "Join the Discord for help: .gg/fBnfWXSDpu"
                ) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = MainWhite)
                }
            }
        }
    }
}

@Composable
fun AlwaysOnTopButton() {
    var tint by remember { mutableStateOf(if (AppWindow.isAlwaysOnTop) MainWhite.copy(alpha = .99f).am else MainWhite) }

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        0f,
        360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Restart
        )
    )

    var windowOnTop by remember { mutableStateOf(AppWindow.isAlwaysOnTop) }
    Tooltip(
        "Toggles the overlay being always on top of all other windows"
    ) {
        Icon(
            Icons.Filled.Star, contentDescription = null, tint = tint,
            modifier = Modifier
                .rotate(if (windowOnTop) rotation else 0f)
                .clickable {
                    AppWindow.isAlwaysOnTop = !(AppWindow.isAlwaysOnTop)
                    WindowState[IsAlwaysOnTop] = windowOnTop.toString()
                    windowOnTop = AppWindow.isAlwaysOnTop

                    tint = if (windowOnTop) MainWhite.copy(alpha = .9f).am else MainWhite
                }
        )
    }
}

@Composable
fun ClearPlayersButton() {
    Tooltip(
        "Deletes all players"
    ) {
        Icon(
            Icons.Filled.Clear, contentDescription = null, tint = MainWhite,
            modifier = Modifier.clickable {
                Entities.removeAll()
            }
        )
    }
}

@Composable
fun RefreshAllButton() {
    val cs = rememberCoroutineScope()

    Tooltip(
        "Refreshes all players"
    ) {
        Icon(
            Icons.Filled.Refresh, contentDescription = null, tint = MainWhite,
            modifier = Modifier.clickable {
                Entities.refreshAll(cs)
            }
        )
    }
}

@Composable
fun AdditionalSettingsBox(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .width(30.tbsm.dp)
            .height(170.tbsm.dp)
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
