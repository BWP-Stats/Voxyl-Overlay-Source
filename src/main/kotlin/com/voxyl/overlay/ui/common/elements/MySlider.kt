package com.voxyl.overlay.ui.common.elements

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
fun MySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = MainPurpleMoreOpaque,
        disabledThumbColor = Color.Transparent,
        activeTrackColor = MainPurple,
        inactiveTrackColor = MainPurpleLessOpaque,
        disabledActiveTrackColor = MainWhite.copy(alpha = 0.1f),
        disabledInactiveTrackColor = MainPurple.copy(alpha = 0.3f),
        activeTickColor = MainWhite.copy(alpha = 0.6f),
        inactiveTickColor = MainWhiteLessOpaque,
        disabledActiveTickColor = MainWhiteLessOpaque,
        disabledInactiveTickColor = MainWhite.copy(alpha = 0.3f)
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