package com.voxyl.overlay.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.R
import com.voxyl.overlay.settings.config.ConfigKeys.G
import com.voxyl.overlay.settings.config.ConfigKeys.B
import com.voxyl.overlay.settings.config.ConfigKeys.Opacity
import com.voxyl.overlay.settings.config.ConfigKeys.BackgroundOpacity
import kotlin.math.roundToInt

var alphaMultiplier = mutableStateOf(Config[Opacity].toFloatOrNull() ?: 1f)

val Color.am
    get() = this.copy((this.alpha * alphaMultiplier.value).coerceIn(0f, 1f))

val Float.amf
    get() = (this * alphaMultiplier.value).coerceIn(0f, 1f)

val MainWhite
    get() = Color(230, 230, 230, 180).am

val ErrorColor
    get() = Color(191, 73, 73, 230).am

private val _MainColor: Color
    get() = Color(
        Config[R].toFloatOrNull()?.roundToInt() ?: 130,
        Config[G].toFloatOrNull()?.roundToInt() ?: 32,
        Config[B].toFloatOrNull()?.roundToInt() ?: 229,
        160
    ).am


val MainColor: MutableState<Color> = mutableStateOf(Color(130, 32, 229, 160).am)
    get() {
        field.value = _MainColor
        return field
    }

var bgAlphaMultiplier = mutableStateOf(Config[BackgroundOpacity].toFloatOrNull() ?: 1f)

val Color.bgam
    get() = this.copy((this.alpha * bgAlphaMultiplier.value).coerceIn(0f, 1f))