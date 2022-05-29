package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.Opacity
import com.voxyl.overlay.ui.elements.VSlider
import com.voxyl.overlay.ui.elements.VText
import com.voxyl.overlay.ui.theme.alphaMultiplier

@Composable
fun OpacitySlider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(8.dp))

        OpacityText(alphaMultiplier)

        Spacer(modifier = Modifier.size(10.dp))

        VSlider(
            value = alphaMultiplier.value,
            onValueChange = {
                alphaMultiplier.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = .3f..3f,
            onValueChangeFinished = {
                Config[Opacity] = alphaMultiplier.value.toString()
            },
        )
    }
}

@Composable
private fun OpacityText(opacity: MutableState<Float>) {
    VText("Opacity (${String.format("%.1f", opacity.value)}x)")
    Spacer(modifier = Modifier.size(17.dp))
}