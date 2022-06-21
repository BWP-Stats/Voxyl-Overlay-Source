package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.BackgroundOpacity
import com.voxyl.overlay.ui.elements.VSlider
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.controllers.common.ui.bgAlphaMultiplier

@Composable
fun BackgroundOpacitySlider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        OpacityText(bgAlphaMultiplier)

        Spacer(modifier = Modifier.size(10.dp))

        VSlider(
            value = bgAlphaMultiplier.value,
            onValueChange = {
                bgAlphaMultiplier.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..3f,
            onValueChangeFinished = {
                Config[BackgroundOpacity] = bgAlphaMultiplier.value.toString()
            },
        )
    }
}

@Composable
private fun OpacityText(opacity: MutableState<Float>) {
    VText("Bg Opacity (${String.format("%.1f", opacity.value)}x)")
    Spacer(modifier = Modifier.size(17.dp))
}