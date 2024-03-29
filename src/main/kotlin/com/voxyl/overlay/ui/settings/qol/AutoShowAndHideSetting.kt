package com.voxyl.overlay.ui.settings.qol

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voxyl.overlay.business.settings.config.AutoShowAndHide
import com.voxyl.overlay.business.settings.config.AutoShowAndHideDelay
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.ui.elements.VCheckbox
import com.voxyl.overlay.ui.elements.VSlider
import com.voxyl.overlay.ui.elements.VText
import kotlin.math.roundToInt

@Composable
fun AutoShowAndHideCheckBox(autoHide: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VCheckbox(
            checked = autoHide.value,
            onCheckedChange = {
                if (it) {
                    Config[AutoShowAndHide] = "true"
                    autoHide.value = it
                } else {
                    Config[AutoShowAndHide] = "false"
                    autoHide.value = it
                }
            },
            modifier = Modifier.size(12.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        VText("Auto show and hide")
    }
}

@Composable
fun AutoShowAndHideDelaySlider(autoHide: MutableState<Boolean>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var delay by remember {
            mutableStateOf(Config[AutoShowAndHideDelay].toFloatOrNull() ?: 5f)
        }

        Spacer(modifier = Modifier.size(8.dp))

        AutoShowAndHideDelayText(delay)

        Spacer(modifier = Modifier.size(10.dp))

        VSlider(
            value = delay,
            onValueChange = {
                delay = it.roundToInt().toFloat()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = autoHide.value,
            valueRange = 3f..15f,
            steps = 12,
            onValueChangeFinished = {
                Config[AutoShowAndHideDelay] = (delay.roundToInt() * 1000).toString()
            },
        )
    }
}

@Composable
private fun AutoShowAndHideDelayText(delay: Float) = if (delay.toInt() < 10) {
    VText("Auto show and hide delay (${delay.toInt()} sec)")
    Spacer(modifier = Modifier.size(17.dp))
} else {
    VText("Auto show and hide delay (${delay.toInt()} sec)")
    Spacer(modifier = Modifier.size(10.dp))
}
