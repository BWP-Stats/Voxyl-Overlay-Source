package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys
import com.voxyl.overlay.settings.config.ConfigKeys.R
import com.voxyl.overlay.settings.config.ConfigKeys.G
import com.voxyl.overlay.settings.config.ConfigKeys.B
import com.voxyl.overlay.ui.common.elements.MySlider
import com.voxyl.overlay.ui.common.elements.MyText
import com.voxyl.overlay.ui.theme.MainWhite

@Composable
fun RSlider(modifier: Modifier = Modifier) {
    val r = mutableStateOf(Config[R].toFloatOrNull() ?: R.defaultValue.toFloat())
    MainColorSlider(R, r, modifier)
}

@Composable
fun GSlider(modifier: Modifier = Modifier) {
    val g = mutableStateOf(Config[G].toFloatOrNull() ?: G.defaultValue.toFloat())
    MainColorSlider(G, g, modifier)
}

@Composable
fun BSlider(modifier: Modifier = Modifier) {
    val b = mutableStateOf(Config[B].toFloatOrNull() ?: B.defaultValue.toFloat())
    MainColorSlider(B, b, modifier)
}

@Composable
fun MainColorSlider(
    key: ConfigKeys,
    color: MutableState<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        ColorText(key, color)

        Spacer(modifier = Modifier.width(10.dp))

        MySlider(
            value = color.value,
            onValueChange = {
                color.value = it
                Config[key] = color.value.toString()
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..255f,
        )
    }
}

@Composable
private fun ColorText(key: ConfigKeys, color: MutableState<Float>) {
    MyText("${key.name}: (${"${color.value.toInt()}".padStart(3, '0')}x)", color = MainWhite, fontSize = 12.sp)
    Spacer(modifier = Modifier.width(17.dp))
}