package com.voxyl.overlay.ui.common

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter as hoverable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.ui.common.elements.ShapeThatIdkTheNameOf
import com.voxyl.overlay.ui.theme.MainWhite

@ExperimentalComposeUiApi
@Composable
fun AdditionalSettings(
    additionalSettingsEnabled: State<Boolean>,
) {
    val animatedSize by animateDpAsState(
        targetValue = if (additionalSettingsEnabled.value) 170.dp else 0.dp
    )

    Box(
        Modifier
            .absolutePadding(top = 19.dp)
            .height(animatedSize)
            .fillMaxWidth()
    ) {
        if (animatedSize.value < 28) return@Box

        AdditionalSettingsBox(Modifier.align(Alignment.CenterEnd).absolutePadding(right = 25.dp)) {
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

                var starTint by remember { mutableStateOf(MainWhite) }

                Icon(
                    Icons.Filled.Star, contentDescription = null, tint = starTint,
                    modifier = Modifier.clickable {
                        window.isAlwaysOnTop = !(window.isAlwaysOnTop)
                        starTint = if (window.isAlwaysOnTop) MainWhite.copy(alpha = .9f) else MainWhite
                    }
                )

                Spacer(Modifier.height(3.dp))
                Icon(Icons.Filled.Info, contentDescription = null, tint = MainWhite)
            }
        }
    }
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
            .background(color = Color(0, 0, 0, 164))
    ) {
        content()
    }
}