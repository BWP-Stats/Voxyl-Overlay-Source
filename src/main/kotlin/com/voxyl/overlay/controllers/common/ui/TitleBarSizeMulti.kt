package com.voxyl.overlay.controllers.common.ui

import androidx.compose.animation.core.Animatable
import com.voxyl.overlay.business.settings.config.Config
import com.voxyl.overlay.business.settings.config.ConfigKeys.TitleBarSizeMulti

var titleBarSizeMulti = Animatable(defaultTitleBarSizeMulti)

val defaultTitleBarSizeMulti
    get() = Config[TitleBarSizeMulti].toFloatOrNull() ?: .7f

val Float.tbsm
    get() = (this * titleBarSizeMulti.value)

val Int.tbsm
    get() = (this.toFloat() * titleBarSizeMulti.value)
