package com.voxyl.overlay.ui.theme

import androidx.compose.animation.core.Animatable
import com.voxyl.overlay.settings.config.Config
import com.voxyl.overlay.settings.config.ConfigKeys.TitleBarSizeMulti

var titleBarSizeMulti = Animatable(defaultTitleBarSizeMulti)

val defaultTitleBarSizeMulti
    get() = Config[TitleBarSizeMulti].toFloatOrNull() ?: .7f

val Float.tbsm
    get() = (this * titleBarSizeMulti.value)

val Int.tbsm
    get() = (this.toFloat() * titleBarSizeMulti.value)