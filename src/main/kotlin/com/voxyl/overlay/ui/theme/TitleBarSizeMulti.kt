package com.voxyl.overlay.ui.theme

import androidx.compose.animation.core.Animatable

//TODO add a default size variable to it can safely be animated without losing original value
var titleBarSizeMulti = Animatable(.7f)

val Float.tbsm
    get() = (this * titleBarSizeMulti.value)

val Int.tbsm
    get() = (this.toFloat() * titleBarSizeMulti.value)