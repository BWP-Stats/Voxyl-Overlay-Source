package com.voxyl.overlay.ui.settings.elements.qol

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.config.Config
import com.voxyl.overlay.config.ConfigKeys.AutoShowAndHide
import com.voxyl.overlay.config.ConfigKeys.AutoShowAndHideDelay
import com.voxyl.overlay.ui.common.elements.MyCheckbox
import com.voxyl.overlay.ui.common.elements.MySlider
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.theme.MainWhite
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
        MyCheckbox(
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

        Spacer(modifier = Modifier.width(16.dp))

        MyText("Auto show and hide", color = MainWhite, fontSize = 12.sp)
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

        Spacer(modifier = Modifier.width(8.dp))

        autoShowAndHideDelayText(delay)

        Spacer(modifier = Modifier.width(10.dp))

        MySlider(
            value = delay,
            onValueChange = {
                delay = it.roundToInt().toFloat()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = autoHide.value,
            valueRange = 3f..15f,
            steps = 12,
            onValueChangeFinished = {
                Config[AutoShowAndHideDelay] = delay.toString()
            },
        )
    }
}

@Composable
private fun autoShowAndHideDelayText(delay: Float) = if (delay.toInt() < 10) {
    MyText("Auto show and hide delay (${delay.toInt()} sec)", color = MainWhite, fontSize = 12.sp)
    Spacer(modifier = Modifier.width(17.dp))
} else {
    MyText("Auto show and hide delay (${delay.toInt()} sec)", color = MainWhite, fontSize = 12.sp)
    Spacer(modifier = Modifier.width(10.dp))
}
