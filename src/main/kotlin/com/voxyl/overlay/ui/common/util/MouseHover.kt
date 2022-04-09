package com.voxyl.overlay.ui.common.util

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerMoveFilter

@Suppress("SpellCheckingInspection")
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.hoverable(
    onEnter: () -> Boolean,
    onExit: () -> Boolean,
    onMove: (Offset) -> Boolean
) = this.pointerMoveFilter(onEnter = onEnter, onExit = onExit, onMove = onMove)