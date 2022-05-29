package com.voxyl.overlay.ui.settings.appearances

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys
import com.voxyl.overlay.business.settings.config.ConfigKeys.*
import com.voxyl.overlay.ui.elements.VSlider
import com.voxyl.overlay.ui.theme.MainWhite
import com.voxyl.overlay.ui.elements.VText

@Composable
fun RSlider(modifier: Modifier = Modifier) {
    val r = mutableStateOf(Config[R].toIntOrNull()?.toFloat() ?: R.defaultValue.toInt().toFloat())
    MainColorSlider(R, r, modifier)
}

@Composable
fun GSlider(modifier: Modifier = Modifier) {
    val g = mutableStateOf(Config[G].toIntOrNull()?.toFloat() ?: G.defaultValue.toInt().toFloat())
    MainColorSlider(G, g, modifier)
}

@Composable
fun BSlider(modifier: Modifier = Modifier) {
    val b = mutableStateOf(Config[B].toIntOrNull()?.toFloat() ?: B.defaultValue.toInt().toFloat())
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
        Spacer(modifier = Modifier.size(8.dp))

        ColorText(key, color)

        Spacer(modifier = Modifier.size(10.dp))

        VSlider(
            value = color.value,
            onValueChange = {
                color.value = it
                Config[key] = color.value.toInt().toString()
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..255f
        )
    }
}

@Composable
private fun ColorText(key: ConfigKeys, color: MutableState<Float>) {
    VText("${key.name}: (${"${color.value.toInt()}".padStart(3, '0')}x)", color = MainWhite, fontSize = 12.sp)
    Spacer(modifier = Modifier.size(17.dp))
}