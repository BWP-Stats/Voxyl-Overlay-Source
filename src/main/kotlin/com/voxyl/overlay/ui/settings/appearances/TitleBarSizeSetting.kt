package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.TitleBarSizeMulti
import com.voxyl.overlay.controllers.common.ui.titleBarSizeMulti
import com.voxyl.overlay.ui.elements.VSlider
import com.voxyl.overlay.ui.elements.VText
import kotlinx.coroutines.launch

@Composable
fun TitleBarSizeSlider(modifier: Modifier = Modifier) {
    val cs = rememberCoroutineScope()

    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        TitleBarSizeMultiText(titleBarSizeMulti)

        Spacer(modifier = Modifier.size(10.dp))

        VSlider(
            value = titleBarSizeMulti.value,
            onValueChange = {
                cs.launch {
                    titleBarSizeMulti.snapTo(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = .7f..1.4f,
            onValueChangeFinished = {
                Config[TitleBarSizeMulti] = titleBarSizeMulti.value.toString()
            },
        )
    }
}

@Composable
private fun TitleBarSizeMultiText(opacity: Animatable<Float, AnimationVector1D>) {
    VText("Title bar size multi (${String.format("%.2f", opacity.value)}x)")
    Spacer(modifier = Modifier.size(17.dp))
}
