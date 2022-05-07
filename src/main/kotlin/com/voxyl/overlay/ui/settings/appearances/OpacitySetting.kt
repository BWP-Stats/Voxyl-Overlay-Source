package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.Opacity
import com.voxyl.overlay.ui.common.elements.MySlider
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.theme.MainWhite
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
        Spacer(modifier = Modifier.width(8.dp))

        OpacityText(alphaMultiplier)

        Spacer(modifier = Modifier.width(10.dp))

        MySlider(
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
    MyText("Opacity (${String.format("%.1f", opacity.value)}x)", color = MainWhite, fontSize = 12.sp)
    Spacer(modifier = Modifier.width(17.dp))
}