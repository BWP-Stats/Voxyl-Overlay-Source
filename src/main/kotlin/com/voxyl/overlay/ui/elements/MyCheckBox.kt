package com.voxyl.overlay.ui.elements

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.voxyl.overlay.ui.theme.*


@Composable
fun MyCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: CheckboxColors = CheckboxDefaults.colors(
        checkedColor = MainColor.value,
        uncheckedColor = MainWhite,
        checkmarkColor = MainWhite,
        disabledColor = MainWhite.copy(alpha = .313f).am,
        disabledIndeterminateColor = MaterialTheme.colors.secondary.copy(alpha = ContentAlpha.disabled)
    )
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors
    )
}