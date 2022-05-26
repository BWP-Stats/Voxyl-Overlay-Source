package com.voxyl.overlay.ui.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.ui.theme.*

@Composable
fun VSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = MainColor.value.copy(alpha = .94f).am,
        disabledThumbColor = Color.Transparent,
        activeTrackColor = MainColor.value,
        inactiveTrackColor = MainColor.value.copy(alpha = 0.43f).am,
        disabledActiveTrackColor = MainWhite.copy(alpha = 0.1f).am,
        disabledInactiveTrackColor = MainColor.value.copy(alpha = 0.3f).am,
        activeTickColor = MainWhite.copy(alpha = 0.6f).am,
        inactiveTickColor = MainWhite.copy(alpha = .313f).am,
        disabledActiveTickColor = MainWhite.copy(alpha = .313f).am,
        disabledInactiveTickColor = MainWhite.copy(alpha = 0.3f).am
    )
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        colors = colors
    )
}