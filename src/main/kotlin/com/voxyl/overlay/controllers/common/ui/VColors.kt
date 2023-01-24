package com.voxyl.overlay.controllers.common.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.voxyl.overlay.business.settings.config.*
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
        Config[PrimaryColorR].toFloatOrNull()?.roundToInt() ?: 130,
        Config[PrimaryColorG].toFloatOrNull()?.roundToInt() ?: 32,
        Config[PrimaryColorB].toFloatOrNull()?.roundToInt() ?: 229,
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
