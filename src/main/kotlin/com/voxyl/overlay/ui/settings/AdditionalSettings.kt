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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.Window
import com.voxyl.overlay.data.homemadesimplecache.HomemadeCache
import com.voxyl.overlay.settings.window.SavedWindowState
import com.voxyl.overlay.settings.window.SavedWindowStateKeys.IsAlwaysOnTop
import com.voxyl.overlay.middleman.PlayerKindaButNotExactlyViewModel
import com.voxyl.overlay.ui.common.elements.ShapeThatIdkTheNameOf
import com.voxyl.overlay.ui.common.util.requestFocusOnClick
import com.voxyl.overlay.ui.theme.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import androidx.compose.ui.input.pointer.pointerMoveFilter as hoverable

@ExperimentalComposeUiApi
@Composable
fun AdditionalSettings(
    additionalSettingsEnabled: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    val cs = rememberCoroutineScope()
    val boxOffset = remember { Animatable(0f)}
    val animatedSize = remember { Animatable(0f) }

    LaunchedEffect(additionalSettingsEnabled.value) {
        if (additionalSettingsEnabled.value) {
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
                if (it.x < Window.size.width - 60 || it.y > 170) {
                    cs.launch {
                        titleBarSizeMulti.animateTo(defaultTitleBarSizeMulti)
                    }
                    additionalSettingsEnabled.value = false
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

    var windowOnTop by remember { mutableStateOf(Window.isAlwaysOnTop) }

    Icon(
        Icons.Filled.Star, contentDescription = null, tint = tint,
        modifier = Modifier
            .rotate(if (windowOnTop) rotation else 0f)
            .clickable {
                Window.isAlwaysOnTop = !(Window.isAlwaysOnTop)
                SavedWindowState[IsAlwaysOnTop] = windowOnTop.toString()
                windowOnTop = Window.isAlwaysOnTop

                tint = if (windowOnTop) MainWhite.copy(alpha = .9f).am else MainWhite
            }
    )
}

@Composable
fun ClearPlayersButton() {
    Icon(
        Icons.Filled.Clear, contentDescription = null, tint = MainWhite,
        modifier = Modifier.clickable {
            HomemadeCache.clear()
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
            HomemadeCache.clear()
            PlayerKindaButNotExactlyViewModel.refreshAll(cs)
        }
    )
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