package com.voxyl.overlay.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.Opacity

var alphaMultiplier = mutableStateOf(Config[Opacity].toFloatOrNull() ?: 1f)

val Color.am
    get() = this.copy((this.alpha * alphaMultiplier.value).coerceIn(0f, 1f))

val Float.amf
    get() = (this * alphaMultiplier.value).coerceIn(0f, 1f)

val MainPurple
    get() = Color(130, 32, 229, 160).am

val MainWhite
    get() = Color(230, 230, 230, 180).am

val ErrorColor
    get() = Color(191, 73, 73, 230).am
